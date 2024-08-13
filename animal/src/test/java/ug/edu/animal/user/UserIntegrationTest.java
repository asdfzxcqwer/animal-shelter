package ug.edu.animal.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import ug.edu.animal.security.service.SecurityService;
import ug.edu.animal.user.service.UserService;
import ug.edu.animal.user.service.serviceimpl.UserRegistrationInput;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserIntegrationTest {
    @Autowired
    private UserService userService;
    @LocalServerPort
    int port;
    @Autowired
    private SecurityService securityService;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("register happy path")
    void register() {
        given().accept(ContentType.JSON)
                .body("""
                        {
                            "email": "komolcia@gmail.com",
                            "password": "Password1!"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("/api/v1/authorization/register")
                .then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(1))
                .body("email", equalTo("komolcia@gmail.com"));
    }

    @Test
    @DisplayName("login happy path")
    void login() {
        UserRegistrationInput userRegistrationInput = UserRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        userService.registerUser(userRegistrationInput);
        given().accept(ContentType.JSON)
                .body("""
                        {
                            "email": "komolcia@gmail.com",
                            "password": "Password1!"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("/api/v1/authorization/login")
                .then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(1))
                .body("email", equalTo("komolcia@gmail.com"));
    }
}
