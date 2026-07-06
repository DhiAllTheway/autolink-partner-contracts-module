<?php

namespace App\Repository;

use App\Entity\Entreprise;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class EntrepriseRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Entreprise::class);
    }

    public function findOneByEmailAndRole(string $email, string $role): ?Entreprise
    {
        return $this->createQueryBuilder('e')
            ->andWhere('e.email = :email')
            ->setParameter('email', $email)
            ->join('e.role', 'r') // Join the Role entity
            ->andWhere('r.name = :role') // Check the role name
            ->setParameter('role', $role)
            ->getQuery()
            ->getOneOrNullResult();
    }
}