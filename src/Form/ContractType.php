<?php

namespace App\Form;

use App\Entity\Contract;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; // For contract content
use Symfony\Bridge\Doctrine\Form\Type\EntityType; // For selecting the receiver
use App\Entity\Entreprise; // Import the Entreprise Entity

class ContractType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('content', TextareaType::class, [
                'label' => 'Contract Content',
                'attr' => ['rows' => 10], // Adjust as needed
            ])
            ->add('receiver', EntityType::class, [ // Add this for the receiver
                'class' => Entreprise::class,
                'choice_label' => 'company_name', // Display company name in dropdown
                'label' => 'Receiver Enterprise',
                'placeholder' => 'Select a receiver',
                'attr' => ['class' => 'form-control'] // Add a class for styling (optional)
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Contract::class,
        ]);
    }
}