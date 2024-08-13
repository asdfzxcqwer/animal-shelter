package ug.edu.breeder.breeder.service.serviceimpl;

import lombok.Builder;

@Builder
public record BreederRegistrationInput(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
