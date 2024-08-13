package ug.edu.breeder.breeder;

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
import ug.edu.breeder.animal.persistance.Animal;
import ug.edu.breeder.breeder.persistance.Breeder;
import ug.edu.breeder.breeder.service.BreederService;
import ug.edu.breeder.breeder.service.serviceimpl.BreederRegistrationInput;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BreederIntegrationTest {
    @Autowired
    private BreederService breederService;
    @LocalServerPort
    int port;

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
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        breederService.registerBreeder(breederRegistrationInput);
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

    @Test
    @DisplayName("deregister breeder happy path")
    void deregister() {
        BreederRegistrationInput breederRegistrationInput = BreederRegistrationInput.builder()
                .email("komolcia@gmail.com")
                .password("Password1!")
                .build();
        breederService.registerBreeder(breederRegistrationInput);
        given().accept(ContentType.JSON)
                .body("""
                        {
                            "email": "komolcia@gmail.com",
                            "password": "Password1!"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().delete("/api/v1/authorization/1")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }
}
