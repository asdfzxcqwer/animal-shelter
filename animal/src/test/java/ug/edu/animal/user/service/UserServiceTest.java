package ug.edu.animal.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ug.edu.animal.user.exception.UserException;
import ug.edu.animal.user.exception.UserExceptionMessage;
import ug.edu.animal.user.persistance.User;
import ug.edu.animal.user.persistance.UserRepository;
import ug.edu.animal.user.service.serviceimpl.UserLoginInput;
import ug.edu.animal.user.service.serviceimpl.UserOutput;
import ug.edu.animal.user.service.serviceimpl.UserRegistrationInput;
import ug.edu.animal.user.service.serviceimpl.UserServiceImpl;
import ug.edu.animal.common.TimeSupplier;
import ug.edu.animal.security.service.serviceimpl.SecurityServiceImpl;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    SecurityServiceImpl securityService;
    @Mock
    TimeSupplier timeSupplier;
    @Mock
    BCryptPasswordEncoder passwordEncoder;


    @Test
    @DisplayName("should register")
    void shouldRegister() {
        //given

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserRegistrationInput userRegistrationInput = UserRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        String encodedPassword = new BCryptPasswordEncoder().encode("Password1!");
        Mockito.doReturn(encodedPassword).when(passwordEncoder).encode(anyString());
        when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));
        UserOutput userOutput = userService.registerUser(userRegistrationInput);
        //then
        assertThat(userOutput).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("should not register and throw EMAIL_ALREADY_REGISTERED")
    void shouldThrowEMAIL_ALREADY_REGISTEREDWhileRegistering() {
        //given
        UserRegistrationInput userRegistrationInput = UserRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        when(userRepository.findByEmail("komolcia@gmail.com")).thenReturn(List.of(User.builder()
                .email("komolcia@gmail.com")
                .password("jakies")
                .build()));

        //then
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() ->
                        userService.registerUser(userRegistrationInput))
                .withMessage(UserExceptionMessage.EMAIL_ALREADY_REGISTERED.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("should not register and throw PASSWORD_INVALID")
    void shouldThrowPASSWORD_INVALIDWhileRegistering() {
        //given
        UserRegistrationInput userRegistrationInput = UserRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password")
                .build();

        //then
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() ->
                        userService.registerUser(userRegistrationInput))
                .withMessage(UserExceptionMessage.PASSWORD_INVALID.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("should not register and throw EMAIL_INVALID")
    void shouldThrowEMAIL_INVALIDWhileRegistering() {
        //given
        UserRegistrationInput userRegistrationInput = UserRegistrationInput.builder()
                .email("komolcia@.com")
                .password("Password1!")
                .build();

        //then
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() ->
                        userService.registerUser(userRegistrationInput))
                .withMessage(UserExceptionMessage.EMAIL_INVALID.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("should Login")
    void shouldLogin() {
        //given
        UserLoginInput userInput = UserLoginInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        when(userRepository.findByEmail("komolcia@gmail.com")).thenReturn(List.of(User.builder()
                .password("Password1!")
                .email("komolcia@gmail.com")
                .build()));
        UserOutput UserOutput = userService.loginUser(userInput);
        //then
        assertThat(UserOutput).isNotNull();
    }

    @Test
    @DisplayName("should not login and throw PASSWORD_OR_EMAIL_DO_NOT_MATCH")
    void shouldThrowPASSWORD_OR_EMAIL_DO_NOT_MATCHWhileLogin() {
        //given
        UserLoginInput userInput = UserLoginInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();

        //then
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() ->
                        userService.loginUser(userInput))
                .withMessage(UserExceptionMessage.PASSWORD_OR_EMAIL_DO_NOT_MATCH.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


}
