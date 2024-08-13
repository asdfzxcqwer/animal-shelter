package ug.edu.animal.user.service.serviceimpl;

import ug.edu.animal.user.persistance.User;

public class UserMapper {
    public User mapToUserFromRegistration(UserRegistrationInput userRegistrationInput) {
        return User.builder()
                .email(userRegistrationInput.email())
                .firstName(userRegistrationInput.firstName())
                .lastName(userRegistrationInput.lastName())
                .password(userRegistrationInput.password())
                .build();
    }

    public UserOutput mapToUserOutput(User user) {
        return UserOutput.builder()
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}
