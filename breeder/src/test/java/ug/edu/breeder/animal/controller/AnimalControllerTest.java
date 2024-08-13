package ug.edu.breeder.animal.controller;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ug.edu.breeder.animal.exception.AnimalException;
import ug.edu.breeder.animal.persistance.Animal;
import ug.edu.breeder.animal.persistance.AnimalStatus;
import ug.edu.breeder.animal.persistance.AnimalType;
import ug.edu.breeder.animal.persistance.Price;
import ug.edu.breeder.animal.service.AnimalService;
import ug.edu.breeder.animal.service.serviceimpl.AnimalInput;
import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;
import ug.edu.breeder.common.TimeSupplier;
import ug.edu.breeder.config.TestSecurityConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static java.time.Month.JANUARY;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = TestSecurityConfig.class)
@AutoConfigureMockMvc
class AnimalControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AnimalService animalService;
    @Autowired
    TimeSupplier timeSupplier;

    @Test
    @DisplayName("animal was added with http status created")
    void shouldCreateAnimalWithHttpStatusCreated() throws Exception {
        when(animalService.createAnimal(any(AnimalInput.class))).thenReturn(getAnimalOutput());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/animals")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.animalType", equalTo("DOG")))
                .andExpect(jsonPath("$.breed", equalTo("cavalier")))
                .andExpect(jsonPath("$.animalStatus", equalTo("AVAILABLE")));
    }

    @Test
    @DisplayName("animal was not created it should throw AnimalException ")
    void shouldThrowExceptionWhileCreatingAnimal() throws Exception {
        when(animalService.createAnimal(any(AnimalInput.class))).thenThrow(AnimalException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/animals")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("animal was gotten with http status ok")
    void shouldGetAnimalWithHttpStatusOk() throws Exception {
        // given
        when(animalService.getAnimal(1L)).thenReturn(getAnimalOutput());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/animals/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.animalType", equalTo("DOG")))
                .andExpect(jsonPath("$.breed", equalTo("cavalier")))
                .andExpect(jsonPath("$.animalStatus", equalTo("AVAILABLE")));
    }

    @Test
    @DisplayName("while getting animal it should throw animal exception")
    void shouldThrowExceptionWhileGettingAnimal() throws Exception {
        when(animalService.getAnimal(any())).thenThrow(AnimalException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/animals/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
//    @DisplayName("animals was gotten with http status ok")
//    void shouldGetAllAnimalsWithHttpStatusOk() throws Exception {
//        when(animalService.getAnimalsByBreederId(1L)).thenReturn(getAnimalsOutput());
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/animals")
//                .contentType("application/json")
//                .content(getValidContentTwoElements()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$[0].id", equalTo(1)))
//                .andExpect(jsonPath("$[0].animalType", equalTo("DOG")))
//                .andExpect(jsonPath("$[0].breed", equalTo("cavalier")))
//                .andExpect(jsonPath("$[0].animalStatus", equalTo("AVAILABLE")))
//                .andExpect(jsonPath("$[1].id", equalTo(2)))
//                .andExpect(jsonPath("$[1].animalType", equalTo("DOG")))
//                .andExpect(jsonPath("$[1].breed", equalTo("cocker spaniel")))
//                .andExpect(jsonPath("$[1].animalStatus", equalTo("AVAILABLE")));
//
//    }



    AnimalOutput getAnimalOutput() {
        return new AnimalOutput(
                1L,
                AnimalStatus.AVAILABLE,
                AnimalType.DOG,
                "cavalier",
                timeSupplier.get().minusYears(2),
                new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
        );
    }

    List<AnimalOutput> getAnimalsOutput(){
        return Arrays.asList(
                new AnimalOutput(1L, AnimalStatus.AVAILABLE, AnimalType.DOG, "cavalier", timeSupplier.get().minusYears(2),  new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
                ), new AnimalOutput(2L, AnimalStatus.AVAILABLE, AnimalType.DOG, "cocker spaniel",timeSupplier.get(),  new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
        );
    }

    private String getValidContent() {
        return """
                {
                    "animalType": "DOG",
                    "animalStatus": "AVAILABLE",
                    "breed": "cocker spaniel",
                    "dateOfBirth":[
                            2000,
                            1,
                            1,
                            10,
                            24
                        ],
                        "breederId": 2,
                          "price": {
                                "amount": 10,
                                "currency": "PLN"
                            }
                }
                """;
    }
    private String getValidContentTwoElements() {
        return """
                [{
                    "animalType": "DOG",
                    "animalStatus": "AVAILABLE",
                    "breed": "cavalier",
                    "dateOfBirth":[
                            2000,
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
                },
                {
                "animalType": "DOG",
                    "animalStatus": "AVAILABLE",
                    "breed": "cavalier",
                    "dateOfBirth":[
                            2000,
                            1,
                            1,
                            10,
                            24
                        ],
                        "breederId": 1,
                          "price": {
                                "amount": 10,
                                "currency": "PLN"
                            },
                }]
                """;
    }

    @Test
    @DisplayName("animal was updated with http status ok")
    void shouldUpdateAnimalWithHttpStatusOk() throws Exception {
        when(animalService.updateAnimal(any(Long.class), any(AnimalInput.class))).thenReturn(getAnimalOutput());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/animals/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.animalType", equalTo("DOG")))
                .andExpect(jsonPath("$.breed", equalTo("cavalier")))
                .andExpect(jsonPath("$.animalStatus", equalTo("AVAILABLE")));
    }
    @Test
    @DisplayName("animal was not updated it should throw AnimalException ")
    void shouldThrowExceptionWhileUpdatingAnimal() throws Exception {
        when(animalService.updateAnimal(any(Long.class),any(AnimalInput.class))).thenThrow(AnimalException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/animals/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("animal was deleted with http status ok")
    void shouldDeleteAnimalWithHttpStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/animals/1")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
