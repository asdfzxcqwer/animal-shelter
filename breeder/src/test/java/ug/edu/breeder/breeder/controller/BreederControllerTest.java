package ug.edu.breeder.breeder.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ug.edu.breeder.breeder.exception.BreederException;
import ug.edu.breeder.breeder.service.BreederService;
import ug.edu.breeder.breeder.service.serviceimpl.BreederLoginInput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederOutput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederRegistrationInput;
import ug.edu.breeder.common.TimeSupplier;
import ug.edu.breeder.security.service.SecurityService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BreederControllerTest {

    @MockBean
    BreederService breederService;
    @MockBean
    SecurityService securityService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TimeSupplier timeSupplier;


    @Test
    @DisplayName("user successfully registered with 200 status")
    void shouldCreateRegistration() throws Exception {
        when(breederService.registerBreeder(any(BreederRegistrationInput.class))).thenReturn(getBreederOutput());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/register")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email", equalTo("komolcia@gmail.com")))
                .andExpect(jsonPath("$.id", equalTo(1)));
    }
    @Test
    @DisplayName("user successfully deregister with http status Unauthorized")
    void shouldDeregisterUserWithHttpStatusOk() throws Exception {
        when(breederService.loginBreeder(any(BreederLoginInput.class))).thenReturn(getBreederOutput());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authorization/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @DisplayName("user successfully logged in with 200 status")
    void shouldLogin() throws Exception {
        when(breederService.loginBreeder(any(BreederLoginInput.class))).thenReturn(getBreederOutput());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/login")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email", equalTo("komolcia@gmail.com")))
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @DisplayName("registration was not created it should throw BreederException")
    void shouldThrowExceptionWhileRegistering() throws Exception {
        when(breederService.registerBreeder(any(BreederRegistrationInput.class))).thenThrow(BreederException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/register")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.time", equalTo(timeSupplier.get().toString())));
    }

    @Test
    @DisplayName("you can not log in it should throw BreederException")
    void shouldThrowExceptionWhileLogin() throws Exception {
        when(breederService.loginBreeder(any(BreederLoginInput.class))).thenThrow(BreederException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/login")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.time", equalTo(timeSupplier.get().toString())));
    }

    private String getValidContent() {
        return """
                {
                    "email": "komolcia@gmail.com",
                    "password": "Password1!"
                }
                """;
    }

    private BreederOutput getBreederOutput() {
        return BreederOutput.builder()
                .email("komolcia@gmail.com")
                .id(1L)
                .build();
    }
}
