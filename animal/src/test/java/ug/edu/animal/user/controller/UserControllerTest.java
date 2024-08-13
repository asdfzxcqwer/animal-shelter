package ug.edu.animal.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ug.edu.animal.common.TimeSupplier;
import ug.edu.animal.security.service.SecurityService;
import ug.edu.animal.user.exception.UserException;
import ug.edu.animal.user.service.UserService;
import ug.edu.animal.user.service.serviceimpl.UserRegistrationInput;
import ug.edu.animal.user.service.serviceimpl.UserLoginInput;
import ug.edu.animal.user.service.serviceimpl.UserOutput;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    SecurityService securityService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TimeSupplier timeSupplier;


    @Test
    @DisplayName("user successfully registered with 200 status")
    void shouldCreateRegistration() throws Exception {
        when(userService.registerUser(any(UserRegistrationInput.class))).thenReturn(getUserOutput());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/register")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email", equalTo("komolcia@gmail.com")))
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @DisplayName("user successfully logged in with 200 status")
    void shouldLogin() throws Exception {
        when(userService.loginUser(any(UserLoginInput.class))).thenReturn(getUserOutput());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/login")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email", equalTo("komolcia@gmail.com")))
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @DisplayName("registration was not created it should throw UserException")
    void shouldThrowExceptionWhileRegistering() throws Exception {
        when(userService.registerUser(any(UserRegistrationInput.class))).thenThrow(UserException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/register")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("you can not log in it should throw UserException")
    void shouldThrowExceptionWhileLogin() throws Exception {
        when(userService.loginUser(any(UserLoginInput.class))).thenThrow(UserException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authorization/login")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String getValidContent() {
        return """
                {
                    "email": "komolcia@gmail.com",
                    "password": "Password1!"
                }
                """;
    }

    private UserOutput getUserOutput() {
        return UserOutput.builder()
                .email("komolcia@gmail.com")
                .id(1L)
                .build();
    }
}
