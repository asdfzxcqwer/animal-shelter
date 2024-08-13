package ug.edu.breeder.breeder.service;

import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ug.edu.breeder.animal.exception.AnimalException;
import ug.edu.breeder.animal.exception.AnimalExceptionMessage;
import ug.edu.breeder.breeder.exception.BreederException;
import ug.edu.breeder.breeder.exception.BreederExceptionMessage;
import ug.edu.breeder.breeder.persistance.Breeder;
import ug.edu.breeder.breeder.persistance.BreederRepository;
import ug.edu.breeder.breeder.service.serviceimpl.BreederLoginInput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederOutput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederRegistrationInput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederServiceImpl;
import ug.edu.breeder.common.TimeSupplier;
import ug.edu.breeder.security.service.serviceimpl.SecurityServiceImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BreederServiceTest {
    @Mock
    BreederRepository breederRepository;
    @InjectMocks
    BreederServiceImpl breederService;
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
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        String encodedPassword = new BCryptPasswordEncoder().encode("Password1!");
        Mockito.doReturn(encodedPassword).when(passwordEncoder).encode(anyString());
        when(breederRepository.save(any(Breeder.class))).thenAnswer(answer -> answer.getArgument(0));
        BreederOutput breederOutput = breederService.registerBreeder(breederRegistrationInput);
        //then
        assertThat(breederOutput).isNotNull();
        verify(breederRepository).save(any(Breeder.class));
    }

    @Test
    @DisplayName("should not register and throw EMAIL_ALREADY_REGISTERED")
    void shouldThrowEMAIL_ALREADY_REGISTEREDWhileRegistering() {
        //given
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        when(breederRepository.findByEmail("komolcia@gmail.com")).thenReturn(List.of(Breeder.builder()
                .email("komolcia@gmail.com")
                .password("jakies")
                .build()));

        //then
        assertThatExceptionOfType(BreederException.class)
                .isThrownBy(() ->
                        breederService.registerBreeder(breederRegistrationInput))
                .withMessage(BreederExceptionMessage.EMAIL_ALREADY_REGISTERED.getMessage());
        verify(breederRepository, never()).save(any(Breeder.class));
    }

    @Test
    @DisplayName("should not register and throw PASSWORD_INVALID")
    void shouldThrowPASSWORD_INVALIDWhileRegistering() {
        //given
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password")
                .build();

        //then
        assertThatExceptionOfType(BreederException.class)
                .isThrownBy(() ->
                        breederService.registerBreeder(breederRegistrationInput))
                .withMessage(BreederExceptionMessage.PASSWORD_INVALID.getMessage());
        verify(breederRepository, never()).save(any(Breeder.class));
    }

    @Test
    @DisplayName("should not register and throw EMAIL_INVALID")
    void shouldThrowEMAIL_INVALIDWhileRegistering() {
        //given
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@.com")
                .password("Password1!")
                .build();

        //then
        assertThatExceptionOfType(BreederException.class)
                .isThrownBy(() ->
                        breederService.registerBreeder(breederRegistrationInput))
                .withMessage(BreederExceptionMessage.EMAIL_INVALID.getMessage());
        verify(breederRepository, never()).save(any(Breeder.class));
    }

    @Test
    @DisplayName("should Login")
    void shouldLogin() {
        //given
        BreederLoginInput breederInput = BreederLoginInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        //when
        when(breederRepository.findByEmail("komolcia@gmail.com")).thenReturn(List.of(Breeder.builder()
                .password("Password1!")
                .email("komolcia@gmail.com")
                .build()));
        BreederOutput breederOutput = breederService.loginBreeder(breederInput);
        //then
        assertThat(breederOutput).isNotNull();
    }

    @Test
    @DisplayName("should not login and throw PASSWORD_OR_EMAIL_DO_NOT_MATCH")
    void shouldThrowPASSWORD_OR_EMAIL_DO_NOT_MATCHWhileLogin() {
        //given
        BreederLoginInput breederInput = BreederLoginInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();

        //then
        assertThatExceptionOfType(BreederException.class)
                .isThrownBy(() ->
                        breederService.loginBreeder(breederInput))
                .withMessage(BreederExceptionMessage.PASSWORD_OR_EMAIL_DO_NOT_MATCH.getMessage());
        verify(breederRepository, never()).save(any(Breeder.class));
    }

    @Test
    @DisplayName("should deregister")
    void shouldDeregister() {
        // given
        long id = 1;
        when(breederRepository.existsById(id)).thenReturn(true);
        // when
        breederService.deregisterBreeder(id);
        // then
        verify(breederRepository).deleteById(id);
    }

    @Test
    @DisplayName("while deregistration it should throw BREEDER_NOT_FOUND")
    void deregisterShouldThrowBREEDER_NOT_FOUNDException() {
        // given
        long id = 1;
        when(breederRepository.existsById(id)).thenReturn(false);
        // when
        assertThatExceptionOfType(BreederException.class)
                .isThrownBy(() -> breederService.deregisterBreeder(id))
                .withMessage(BreederExceptionMessage.BREEDER_DOES_NOT_EXIST.getMessage());

        // then
        verify(breederRepository, never()).deleteById(id);
    }
}
