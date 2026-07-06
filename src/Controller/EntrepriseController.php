<?php

namespace App\Controller;

use App\Entity\PartnershipRequest;
use App\Entity\Entreprise;
use App\Form\EntrepriseType;
use App\Form\PartnershipRequestType;



use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorageInterface;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;


class EntrepriseController extends AbstractController
{
    #[Route('/entreprise-login', name: 'entreprise_login')]
    public function login(AuthenticationUtils $authenticationUtils): Response
    {
        $error = $authenticationUtils->getLastAuthenticationError();
        $lastUsername = $authenticationUtils->getLastUsername();
        return $this->render('user/entreprise/login.html.twig', [
            'last_username' => $lastUsername,
            'error' => $error,
        ]);
    }

    #[Route('/entreprise/logoutEntreprise', name: 'entreprise_logout')]
    public function logout(): void
    {
        // Symfony handles logout automatically
    }

    #[Route('/create-account-entreprise', name: 'entreprise_register')]
    public function register(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher): Response
    {
        $entreprise = new Entreprise();
        $form = $this->createForm(EntrepriseType::class, $entreprise);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $hashedPassword = $passwordHasher->hashPassword($entreprise, $entreprise->getPassword());
            $entreprise->setPassword($hashedPassword);
            $entreprise->setRoles(['ROLE_ENTREPRISE']);
            
            $entityManager->persist($entreprise);
            $entityManager->flush();
            
            return $this->redirectToRoute('entreprise_login');
        }

        return $this->render('user/entreprise/register.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/entreprise-dashboard', name: 'entreprise_dashboard')]
public function dashboard(Request $request, EntityManagerInterface $entityManager): Response
{
    // Get the logged-in user
    $user = $this->getUser();

    if (!$user) {
        throw $this->createAccessDeniedException('Vous devez être connecté.');
    }

    // Get search and sort query parameters
    $search = $request->query->get('search', '');
    $sort   = $request->query->get('sort', '');

    // Allowed filtering values
    $allowedSortValues = ['sponsoring', 'recycling', 'education', 'marketing'];

    // Query builder to fetch partnership requests for the logged-in user
    $queryBuilder = $entityManager->getRepository(PartnershipRequest::class)
        ->createQueryBuilder('p')
        ->where('p.entrepriseId = :entrepriseId')
        ->setParameter('entrepriseId', $user->getId());

    // Apply search filter (companyEmail, phone, partnershipType)
    if (!empty($search)) {
        $queryBuilder
            ->andWhere('p.companyEmail LIKE :search OR p.phone LIKE :search OR p.partnershipType LIKE :search')
            ->setParameter('search', '%' . $search . '%');
    }

    // Apply filtering by partnership type
    if (!empty($sort) && in_array($sort, $allowedSortValues)) {
        $queryBuilder->andWhere('p.partnershipType = :sort')
            ->setParameter('sort', $sort);
    }

    // Execute query
    $partnershipRequests = $queryBuilder->getQuery()->getResult();

    return $this->render('user/entreprise/dashboard.html.twig', [
        'partnershipRequests' => $partnershipRequests ?? [],
        'search' => $search,
        'sort' => $sort,
    ]);
}










    #[Route('/partnership-request/{id}/edit', name: 'partnership_request_edit')]
public function edit(Request $request, EntityManagerInterface $entityManager, PartnershipRequest $partnershipRequest): Response
{
    // Get the logged-in user
    $user = $this->getUser();

    // Ensure the request belongs to the logged-in entreprise
    if ($partnershipRequest->getEntrepriseId() !== $user->getId()) {
        throw $this->createAccessDeniedException('Vous n\'avez pas la permission de modifier cette demande.');
    }

    // ✅ Ensure the correct form type is used
    $form = $this->createForm(PartnershipRequestType::class, $partnershipRequest);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager->flush();
        $this->addFlash('success', 'Votre demande de partenariat a été mise à jour avec succès.');
        return $this->redirectToRoute('entreprise_dashboard');
    }

    return $this->render('user/entreprise/edit.html.twig', [
        'form' => $form->createView(),
    ]);
}


    

    


    #[Route('/entreprise/authenticate', name: 'entreprise_authenticate', methods: ['POST'])]
    public function authenticate(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher, TokenStorageInterface $tokenStorage): Response
    {
        $email = $request->request->get('_username');
        $password = $request->request->get('_password');
        
        $entreprise = $entityManager->getRepository(Entreprise::class)->findOneBy(['email' => $email]);
        
        if (!$entreprise || !$passwordHasher->isPasswordValid($entreprise, $password)) {
            $this->addFlash('error', 'Email ou mot de passe invalide. Veuillez réessayer.');
            return $this->redirectToRoute('entreprise_login');
        }
        
        if (!in_array('ROLE_ENTREPRISE', $entreprise->getRoles())) {
            $this->addFlash('error', 'Accès refusé. Rôle non valide.');
            return $this->redirectToRoute('entreprise_login');
        }

        // Create authentication token
        $token = new UsernamePasswordToken($entreprise, 'entreprise', $entreprise->getRoles());
        $tokenStorage->setToken($token);
        
        return $this->redirectToRoute('entreprise_dashboard');
    }
}
