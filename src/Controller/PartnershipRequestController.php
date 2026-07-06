<?php

namespace App\Controller;

use App\Entity\PartnershipRequest;
use App\Form\PartnershipRequestType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;
use chillerlan\QRCode\QRCode;
use chillerlan\QRCode\QROptions;

class PartnershipRequestController extends AbstractController
{
    #[Route('/partnership-request', name: 'partnership_request')]
    public function index(Request $request, EntityManagerInterface $entityManager): Response
    {
        $user = $this->getUser();
    
        if (!$user) {
            throw $this->createAccessDeniedException('Vous devez être connecté pour soumettre une demande.');
        }
    
        if (!in_array('ROLE_ENTREPRISE', $user->getRoles())) {
            $this->addFlash('error', 'Vous devez avoir le rôle ENTREPRISE pour soumettre une demande.');
            return $this->redirectToRoute('entreprise_dashboard');
        }
    
        $search = $request->query->get('search');
        $sort = $request->query->get('sort');
    
        // Query to get partnership requests
        $queryBuilder = $entityManager->getRepository(PartnershipRequest::class)->createQueryBuilder('p')
            ->where('p.entrepriseId = :entrepriseId')
            ->setParameter('entrepriseId', $user->getId());
    
            if ($search) {
                $queryBuilder->andWhere('LOWER(p.tax_code) LIKE LOWER(:search)')
                    ->setParameter('search', '%' . strtolower(trim($search)) . '%');
            }
                
        if ($sort) {
            $queryBuilder->andWhere('p.partnershipType = :sort')
                ->setParameter('sort', $sort);
        }
    
        $partnershipRequests = $queryBuilder->getQuery()->getResult();
    
        // ✅ Create the form and handle submission
        $partnershipRequest = new PartnershipRequest();
        $form = $this->createForm(PartnershipRequestType::class, $partnershipRequest);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Set entreprise ID
            $partnershipRequest->setEntrepriseId($user->getId());
    
            $entityManager->persist($partnershipRequest);
            $entityManager->flush();
    
            $this->addFlash('success', 'Votre demande de partenariat a été soumise avec succès.');
    
            return $this->redirectToRoute('partnership_request_submitted');
        }
    
        return $this->render('partnership_request/index.html.twig', [
            'form' => $form->createView(),
            'partnershipRequests' => $partnershipRequests,
            'search' => $search,
            'sort' => $sort,
        ]);
    }
    

    #[Route('/partnership-request/success', name: 'partnership_request_submitted')]
    public function success(): Response
    {
        return $this->render('partnership_request/success.html.twig');
    }

    #[Route('/partnership-request/{id}/edit', name: 'partnership_request_edit')]
    public function edit(Request $request, EntityManagerInterface $entityManager, PartnershipRequest $partnershipRequest): Response
    {
        $user = $this->getUser();

        if (!$user) {
            throw $this->createAccessDeniedException('Vous devez être connecté.');
        }

        if ($partnershipRequest->getEntrepriseId() !== $user->getId()) {
            throw $this->createAccessDeniedException('Vous n\'avez pas la permission de modifier cette demande.');
        }

        $form = $this->createForm(PartnershipRequestType::class, $partnershipRequest);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $this->addFlash('success', 'Votre demande de partenariat a été mise à jour avec succès.');
            return $this->redirectToRoute('entreprise_dashboard');
        }

        return $this->render('user/entreprise/edit.html.twig', [
            'form' => $form->createView(),
            'partnershipRequest' => $partnershipRequest,
        ]);
    }

    #[Route('/partnership-request/{id}/delete', name: 'partnership_request_delete', methods: ['POST'])]
    public function delete(Request $request, EntityManagerInterface $entityManager, PartnershipRequest $partnershipRequest): Response
    {
        $user = $this->getUser();

        if ($partnershipRequest->getEntrepriseId() !== $user->getId()) {
            throw $this->createAccessDeniedException('Vous n\'avez pas la permission de supprimer cette demande.');
        }

        if ($this->isCsrfTokenValid('delete' . $partnershipRequest->getIdRequest(), $request->request->get('_token'))) {
            $entityManager->remove($partnershipRequest);
            $entityManager->flush();
            $this->addFlash('success', 'Votre demande de partenariat a été supprimée avec succès.');
        } else {
            $this->addFlash('error', 'Échec de la suppression: jeton CSRF invalide.');
        }

        return $this->redirectToRoute('entreprise_dashboard');
    }

    #[Route('/partnership-request/{id}', name: 'partnership_request_show')]
    public function show(PartnershipRequest $partnershipRequest): Response
    {
        $qrText = sprintf(
            "Email: %s | Type de Partenariat: %s | Statut: %s",
            $partnershipRequest->getCompanyEmail(),
            $partnershipRequest->getPartnershipType(),
            $partnershipRequest->getStatus()
        );

        $options = new QROptions([
            'version'    => 10,
            'outputType' => QRCode::OUTPUT_IMAGE_PNG,
            'eccLevel'   => QRCode::ECC_H,
            'scale'      => 5,
            'imageBase64' => false,
        ]);

        $qrCode = new QRCode($options);
        $qrCodeImage = $qrCode->render($qrText);

        $filePath = $this->getParameter('kernel.project_dir') . '/public/qr_codes/qr_code.png';

        if (!is_dir(dirname($filePath))) {
            mkdir(dirname($filePath), 0777, true);
        }

        file_put_contents($filePath, $qrCodeImage);

        $qrCodeBase64 = 'data:image/png;base64,' . base64_encode($qrCodeImage);

        return $this->render('user/entreprise/show.html.twig', [
            'partnershipRequest' => $partnershipRequest,
            'qrCode' => $qrCodeBase64,
        ]);
    }
}
