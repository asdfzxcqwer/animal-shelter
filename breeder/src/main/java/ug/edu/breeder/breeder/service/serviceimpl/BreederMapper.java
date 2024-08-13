package ug.edu.breeder.breeder.service.serviceimpl;

import ug.edu.breeder.breeder.persistance.Breeder;

class BreederMapper {
    public Breeder mapToBreederFromRegistration(BreederRegistrationInput breederRegistrationInput) {
        return Breeder.builder()
                .email(breederRegistrationInput.email())
                .firstName(breederRegistrationInput.firstName())
                .lastName(breederRegistrationInput.lastName())
                .password(breederRegistrationInput.password())
                .build();
    }

    public BreederOutput mapToBreederOutput(Breeder breeder) {
        return BreederOutput.builder()
                .email(breeder.getEmail())
                .id(breeder.getId())
                .build();
    }
}
