package ug.edu.breeder.animal;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ug.edu.breeder.animal.persistance.*;
import ug.edu.breeder.animal.service.AnimalService;
import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;
import ug.edu.breeder.breeder.persistance.Breeder;
import ug.edu.breeder.breeder.persistance.BreederRepository;
import ug.edu.breeder.config.TestSecurityConfig;
import ug.edu.breeder.common.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static io.restassured.RestAssured.given;

import static java.time.Month.JANUARY;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestSecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AnimalIntegrationTest {
    @Autowired
    private AnimalService animalService;
    @Autowired
    private BreederRepository breederRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("add animal happy path")
    void addAnimal() {
        breederRepository.save(Breeder.builder().id(1L).build());
        given().accept(ContentType.JSON).body("""
                        {
                            "animalType": "DOG",
                            "animalStatus": "AVAILABLE",
                            "breed": "cavalier",
                            "dateOfBirth":[
                                    2015,
                                    1,
                                    1,
                                    10,
                                    24
                                ],
                            "breederId": 1,
                            "price": {
                                "amount": 10,
                                "currency": "PLN"
                            }
                        }
                        """).contentType(ContentType.JSON)
                .when().post("/api/v1/animals")
                .then().statusCode(HttpStatus.SC_CREATED)
                .body("animalType", equalTo("DOG"))
                .body("id", equalTo(1))
                .body("animalStatus", equalTo("AVAILABLE"))
                .body("breed", equalTo("cavalier"))
                .body("dateOfBirth", equalTo("2015-01-01T10:24:00"));
    }

    @Test
    @DisplayName("update animal happy path")
    void updateAnimal() {
        breederRepository.save(Breeder.builder().id(1L).build());
        animalRepository.save(Animal.builder().id(1L).build());
        given().accept(ContentType.JSON).body("""
                        {
                            "animalType": "DOG",
                            "animalStatus": "AVAILABLE",
                            "breed": "cavalier",
                            "dateOfBirth":[
                                    2015,
                                    1,
                                    1,
                                    10,
                                    24
                                ],
                            "breederId": 1,
                            "price": {
                                "amount": 10,
                                "currency": "PLN"
                            }
                        }
                        """).contentType(ContentType.JSON)
                .when().put("/api/v1/animals/1")
                .then().statusCode(HttpStatus.SC_OK)
                .body("animalType", equalTo("DOG"))
                .body("id", equalTo(1))
                .body("animalStatus", equalTo("AVAILABLE"))
                .body("breed", equalTo("cavalier"))
                .body("dateOfBirth", equalTo("2015-01-01T10:24:00"));
    }
    @Test
    @DisplayName("delete animal happy path")
    void deleteAnimal() {
        breederRepository.save(Breeder.builder().id(1L).build());
        animalRepository.save(Animal.builder().id(1L).build());
        given().accept(ContentType.JSON).body("""
                        {
                            "animalType": "DOG",
                            "animalStatus": "AVAILABLE",
                            "breed": "cavalier",
                            "dateOfBirth":[
                                    2015,
                                    1,
                                    1,
                                    10,
                                    24
                                ],
                            "breederId": 1,
                            "price": {
                                "amount": 10,
                                "currency": "PLN"
                            }
                        }
                        """).contentType(ContentType.JSON)
                .when().delete("/api/v1/animals/1")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("get animal happy path")
    void getAnimal() {
        breederRepository.save(Breeder.builder().id(1L).build());
        animalRepository.save(Animal.builder().id(1L).animalType(AnimalType.DOG).animalStatus(AnimalStatus.AVAILABLE).breed("cavalier").dateOfBirth(LocalDateTime.of(2015, Month.JANUARY, 1, 10, 24, 00, 0)).breederId(1L).build());
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().get("/api/v1/animals/1")
                .then().statusCode(HttpStatus.SC_OK)
                .body("animalType", equalTo("DOG"))
                .body("id", equalTo(1))
                .body("animalStatus", equalTo("AVAILABLE"))
                .body("breed", equalTo("cavalier"))
                .body("dateOfBirth", equalTo("2015-01-01T10:24:00"));
    }


//    @Test
//    @DisplayName("get animals happy path")
//    void getAnimals() {
//        breederRepository.save(Breeder.builder().id(1L).build());
//        animalRepository.save(Animal.builder().id(1L).animalType(AnimalType.DOG).animalStatus(AnimalStatus.AVAILABLE).breed("cavalier").dateOfBirth(LocalDateTime.of(2015, Month.JANUARY, 1, 10, 24, 00, 0)).breederId(1L).build());
//        animalRepository.save(Animal.builder().id(2L).animalType(AnimalType.DOG).animalStatus(AnimalStatus.AVAILABLE).breed("cavalier").dateOfBirth(LocalDateTime.of(2015, Month.JANUARY, 1, 10, 24, 00, 0)).breederId(1L).build());
//
//        given().accept(ContentType.JSON).contentType(ContentType.JSON)
//                .when().get("/api/v1/animals/1")
//                .then().statusCode(HttpStatus.SC_OK)
//                .body("$.size()", equalTo(2));
//    }
}
