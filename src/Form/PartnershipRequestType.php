<?php

namespace App\Form;

use App\Entity\PartnershipRequest;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;
class PartnershipRequestType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        
            ->add('companyEmail', EmailType::class, [
                'label' => 'Company Email',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter your company email.']),
                    new Email(['message' => 'Please enter a valid email address.']),
                ],
            ])
            ->add('phone', TextType::class, [
                'label' => 'Phone Number',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter your phone number.']),
                    new Length(['min' => 10, 'max' => 15]),
                ],
            ])
            ->add('address', TextType::class, [
                'label' => 'Company Address',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter your company address.']),
                ],
            ])
            ->add('taxCode', TextType::class, [
                'label' => 'Tax Code',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter your tax code.']),
                ],
            ])
            ->add('partnershipType', ChoiceType::class, [
                'label' => 'Partnership Type',
                'choices' => [
                    'Recycling' => 'recycling',
                    'Sponsoring' => 'sponsoring',
                    'Advertising' => 'advertising',
                    'Certified Workshops' => 'certified_workshops',
                ],
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Please select a partnership type.']),
                ],
            ])
            ->add('description', TextareaType::class, [
                'label' => 'Description',
                'attr' => ['class' => 'form-control', 'rows' => 5],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter a description.']),
                    new Length(['min' => 10, 'max' => 1000]),
                ],
            ])
            ->add('submit', SubmitType::class, [
                'label' => 'Submit Request',
                'attr' => ['class' => 'btn btn-dark w-100 py-2', 'style' => 'background-color: #8B0000;']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
{
    $resolver->setDefaults([
        'data_class' => PartnershipRequest::class,
        'csrf_protection' => true,  // ✅ Ensures CSRF protection is enabled
        'csrf_field_name' => '_token',  // ✅ Uses Symfony's default CSRF token field
        'csrf_token_id'   => 'partnership_request',  // ✅ Matches the form's identifier
    ]);
}

}
