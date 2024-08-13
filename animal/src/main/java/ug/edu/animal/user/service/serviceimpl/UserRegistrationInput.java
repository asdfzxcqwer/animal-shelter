package ug.edu.animal.user.service.serviceimpl;

import lombok.Builder;

@Builder
public record UserRegistrationInput(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
