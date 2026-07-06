<?php

namespace App\Controller;

use App\Entity\Contract;
use App\Entity\PartnershipRequest;
use App\Form\ContractType;
use App\Repository\ContractRepository;
use App\Service\TwilioService; // Import the TwilioService
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/contracts')]
final class ContractController extends AbstractController
{
    #[Route('/', name: 'contracts_index', methods: ['GET'])]
    public function index(ContractRepository $contractRepository): Response
    {
        $user = $this->getUser();
        $entreprise = $user->getEntreprise();

        if (!$entreprise) {
            throw $this->createNotFoundException('Vous devez être une entreprise pour accéder aux contrats.');
        }

        $sentContracts = $contractRepository->findBy(['entreprise' => $entreprise]);
        $receivedContracts = $contractRepository->findBy(['receiver' => $entreprise]);

        return $this->render('contract/index.html.twig', [
            'sentContracts' => $sentContracts,
            'receivedContracts' => $receivedContracts,
        ]);
    }

    #[Route('/new', name: 'contracts_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $user = $this->getUser();
        $entreprise = $user->getEntreprise();

        if (!$entreprise) {
            throw $this->createNotFoundException('Vous devez être une entreprise pour créer un contrat.');
        }

        $contract = new Contract();
        $form = $this->createForm(ContractType::class, $contract);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Get the receiver entreprise from the form data
            $receiver = $form->get('receiver')->getData();

            if (!$receiver) {
                $this->addFlash('error', 'Veuillez choisir une entreprise valide comme destinataire.');
                return $this->redirectToRoute('contracts_new');
            }

            // Get the sender entreprise (the one creating the contract)
            $senderEntreprise = $entreprise;

            // Check if a partnership request exists between the sender and receiver with 'Accepted' status
            $partnershipExists = $entityManager->getRepository(PartnershipRequest::class)->findOneBy([
                'entrepriseId' => $senderEntreprise->getId(),
                'status' => 'Accepted'
            ]);

            // Check if the receiver also has an accepted partnership request
            $partnershipReceiverExists = $entityManager->getRepository(PartnershipRequest::class)->findOneBy([
                'entrepriseId' => $receiver->getId(),
                'status' => 'Accepted'
            ]);

            // If no accepted partnership exists for either sender or receiver, show an error
            if (!$partnershipExists || !$partnershipReceiverExists) {
                $this->addFlash('error', 'Les entreprises doivent être des partenaires approuvés pour créer un contrat.');
                return $this->redirectToRoute('contracts_new');
            }

            // Set the contract attributes
            $contract->setEntreprise($senderEntreprise);
            $contract->setReceiver($receiver);
            $contract->setStatus('pending');
            $contract->setContent($form->get('content')->getData());

            // Persist and save the contract
            $entityManager->persist($contract);
            $entityManager->flush();

            $this->addFlash('success', 'Contrat créé avec succès.');
            return $this->redirectToRoute('contracts_index');
        }

        return $this->render('contract/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}/accept', name: 'contracts_accept', methods: ['POST'])]
    public function accept(Contract $contract, EntityManagerInterface $entityManager, TwilioService $twilioService): Response
    {
        if ($contract->getStatus() !== 'pending') {
            $this->addFlash('error', 'Ce contrat ne peut pas être accepté.');
            return $this->redirectToRoute('contracts_index');
        }

        // Update contract status to 'accepted'
        $contract->setStatus('accepted');
        $entityManager->flush();

        // Send WhatsApp notification to the receiver
        $receiverPhoneNumber = $contract->getReceiver()->getPhone(); // Use the `phone` attribute from Entreprise
        $senderCompanyName = $contract->getEntreprise()->getCompanyName(); // Use the `company_name` attribute from Entreprise
        $message = "Votre contrat avec {$senderCompanyName} a été accepté!";
        $twilioService->sendWhatsAppMessage($receiverPhoneNumber, $message);

        $this->addFlash('success', 'Contrat accepté avec succès.');
        return $this->redirectToRoute('contracts_index');
    }
    
    #[Route('/{id}/reject', name: 'contracts_reject', methods: ['POST'])]
    public function rejectContract(Contract $contract, EntityManagerInterface $entityManager, Request $request): Response
    {
        // Set contract status to 'rejected'
        $contract->setStatus('rejected');
        $entityManager->flush();

        // Redirect after rejection
        return $this->redirectToRoute('contracts_index');
    }

    #[Route('/{id}/edit', name: 'contracts_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Contract $contract, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ContractType::class, $contract);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $this->addFlash('success', 'Contrat mis à jour avec succès.');
            return $this->redirectToRoute('contracts_index');
        }

        return $this->render('contract/edit.html.twig', [
            'contract' => $contract,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'contracts_delete', methods: ['POST'])]
    public function delete(Request $request, Contract $contract, EntityManagerInterface $entityManager): Response
    {
        // Check if the CSRF token is valid
        if ($this->isCsrfTokenValid('delete' . $contract->getId(), $request->request->get('_token'))) {
            // Remove the contract from the database
            $entityManager->remove($contract);  // This will delete the contract
            $entityManager->flush();  // Apply changes to the database

            // Add success flash message
            $this->addFlash('success', 'Contrat supprimé.');
        }

        // Redirect back to contracts index
        return $this->redirectToRoute('contracts_index');
    }
}