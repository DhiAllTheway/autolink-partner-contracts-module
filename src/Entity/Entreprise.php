<?php

namespace App\Entity;

use App\Entity\Address;
use App\Entity\Role;
use App\Entity\PartnershipRequest;
use App\Entity\Contract;
use App\Repository\EntrepriseRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EntrepriseRepository::class)]
#[ORM\Table(name: 'entreprise')]
class Entreprise implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank]
    private ?string $company_name = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\Email]
    private ?string $email = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank]
    private ?string $phone = null;

    #[ORM\Embedded(class: Address::class)]
    private Address $address;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank]
    private ?string $tax_code = null;

    #[ORM\ManyToOne(targetEntity: Role::class, inversedBy: 'entreprises')]
    private ?Role $role = null;

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $created_at = null;

    #[ORM\Column(type: 'boolean')]
    private ?bool $supplier = null;

    #[ORM\Column(type: 'string', length: 255)]
    private ?string $password = null;

    #[ORM\Column(length: 255)]
    private ?string $field = null;

    /**
     * @var Collection<int, PartnershipRequest>
     */
    #[ORM\OneToMany(targetEntity: PartnershipRequest::class, mappedBy: 'entreprise')]
    private Collection $partnershipRequests;

        /**
     * @var Collection<int, Contract>
     */
    #[ORM\OneToMany(mappedBy: 'entreprise', targetEntity: Contract::class, orphanRemoval: true)]
    private Collection $contracts;


    public function __construct()
    {
        $this->created_at = new \DateTimeImmutable();
        $this->partnershipRequests = new ArrayCollection(); // Initialisation de la collection
        $this->contracts = new ArrayCollection();
    }
    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCompanyName(): ?string
    {
        return $this->company_name;
    }

    public function setCompanyName(string $company_name): static
    {
        $this->company_name = $company_name;
        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;
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

    public function getAddress(): ?Address
    {
        return $this->address;
    }

    public function setAddress(Address $address): self
    {
        $this->address = $address;
        return $this;
    }

    public function getTaxCode(): ?string
    {
        return $this->tax_code;
    }

    public function setTaxCode(string $tax_code): static
    {
        $this->tax_code = $tax_code;
        return $this;
    }

    public function getRole(): ?Role
    {
        return $this->role;
    }

    public function setRole(?Role $role): static
    {
        $this->role = $role;
        return $this;
    }

    public function eraseCredentials()
    {
        // No credentials to erase
    }

    public function getUserIdentifier(): string
    {
        return $this->getEmail();
    }

    public function getCreatedAt(): ?\DateTimeImmutable
    {
        return $this->created_at;
    }

    public function setCreatedAt(\DateTimeImmutable $created_at): static
    {
        $this->created_at = $created_at;
        return $this;
    }

    public function isSupplier(): ?bool
    {
        return $this->supplier;
    }

    public function setSupplier(bool $supplier): static
    {
        $this->supplier = $supplier;
        return $this;
    }

    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(string $password): static
    {
        $this->password = $password;
        return $this;
    }

    public function getRoles(): array
    {
        return $this->getRole() ? [$this->getRole()->getName()] : [];
    }
    
    public function getField(): ?string
    {
        return $this->field;
    }

    public function setField(string $field): static
    {
        $this->field = $field;
        return $this;
    }

    /**
     * @return Collection<int, PartnershipRequest>
     */
    public function getPartnershipRequests(): Collection
    {
        return $this->partnershipRequests;
    }

    public function addPartnershipRequest(PartnershipRequest $partnershipRequest): static
    {
        if (!$this->partnershipRequests->contains($partnershipRequest)) {
            $this->partnershipRequests->add($partnershipRequest);
            $partnershipRequest->setEntreprise($this);
        }

        return $this;
    }

    public function removePartnershipRequest(PartnershipRequest $partnershipRequest): static
    {
        if ($this->partnershipRequests->removeElement($partnershipRequest)) {
            // set the owning side to null (unless already changed)
            if ($partnershipRequest->getEntreprise() === $this) {
                $partnershipRequest->setEntreprise(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Contract>
     */

     public function getContracts(): Collection
     {
         return $this->contracts;
     }
 
     public function addContract(Contract $contract): static
     {
         if (!$this->contracts->contains($contract)) {
             $this->contracts->add($contract);
             $contract->setEntreprise($this);
         }
 
         return $this;
     }
 
     public function removeContract(Contract $contract): static
     {
         if ($this->contracts->removeElement($contract)) {
             // set the owning side to null (unless already changed)
             if ($contract->getEntreprise() === $this) {
                 $contract->setEntreprise(null);
             }
         }
 
         return $this;
     }
 




}