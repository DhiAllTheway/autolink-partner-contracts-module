<?php

namespace App\Entity;

use App\Repository\PartnershipRequestRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: PartnershipRequestRepository::class)]
class PartnershipRequest
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $idRequest = null;

    #[ORM\Column(nullable: false)] // Store the user ID directly
    private ?int $entrepriseId = null;

    #[ORM\Column(length: 20)]
    private ?string $status = 'Pending';

    #[ORM\Column(length: 255)]
    #[Assert\Email]
    private ?string $companyEmail = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $phone = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $address = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $taxCode = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $partnershipType = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank]
    private ?string $description = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $submittedAt = null;

    public function __construct()
    {
        $this->submittedAt = new \DateTime();
        $this->status = 'Pending';
    }

    public function getIdRequest(): ?int
    {
        return $this->idRequest;
    }

    public function getEntrepriseId(): ?int
    {
        return $this->entrepriseId;
    }

    public function setEntrepriseId(int $entrepriseId): static
    {
        $this->entrepriseId = $entrepriseId;
        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->status;
    }

    public function setStatus(string $status): static
    {
        $this->status = $status;
        return $this;
    }

    public function getCompanyEmail(): ?string
    {
        return $this->companyEmail;
    }

    public function setCompanyEmail(string $companyEmail): static
    {
        $this->companyEmail = $companyEmail;
        return $this;
    }

    public function getPhone(): ?string
    {
        return $this->phone;
    }

    public function setPhone(string $phone): static
    {
        $this->phone = $phone;
        return $this;
    }

    public function getAddress(): ?string
    {
        return $this->address;
    }

    public function setAddress(string $address): static
    {
        $this->address = $address;
        return $this;
    }

    public function getTaxCode(): ?string
    {
        return $this->taxCode;
    }

    public function setTaxCode(string $taxCode): static
    {
        $this->taxCode = $taxCode;
        return $this;
    }

    public function getPartnershipType(): ?string
    {
        return $this->partnershipType;
    }

    public function setPartnershipType(string $partnershipType): static
    {
        $this->partnershipType = $partnershipType;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;
        return $this;
    }

    public function getSubmittedAt(): ?\DateTimeInterface
    {
        return $this->submittedAt;
    }

    public function setSubmittedAt(\DateTimeInterface $submittedAt): static
    {
        $this->submittedAt = $submittedAt;
        return $this;
    }
}
