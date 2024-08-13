package ug.edu.animal.animal.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ug.edu.animal.animal.exception.AnimalException;
import ug.edu.animal.animal.persistance.AnimalStatus;
import ug.edu.animal.animal.persistance.AnimalType;
import ug.edu.animal.animal.persistance.Price;
import ug.edu.animal.animal.service.AnimalService;
import ug.edu.animal.animal.service.serviceimpl.AnimalOutput;
import ug.edu.animal.common.TimeSupplier;
import ug.edu.animal.config.TestSecurityConfig;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

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

    @Test
    @DisplayName("while getting animals by status it should throw animal exception")
    void shouldThrowExceptionWhileGettingAnimalByStatus() throws Exception {
        when(animalService.getAnimalsByStatus(any(String.class), any(Pageable.class))).thenThrow(AnimalException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/animals?status=INCORECT")
                        .contentType("application/json")
                        .content(getValidContent()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("animals were gotten with http status ok")
    void shouldGetAllAnimalsByStatusWithHttpStatusOk() throws Exception {
        PageImpl<AnimalOutput> value = new PageImpl<>(List.of(getAnimalOutput()));
        int pageNumber = 0;
        int pageSize = 1;
        PageRequest defaultPageRequest = PageRequest.of(pageNumber, pageSize);
        when(animalService.getAnimalsByStatus("AVAILABLE", defaultPageRequest)).thenReturn(value);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/animals?status=AVAILABLE" + "&page=" + pageNumber + "&size=" + pageSize))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size", equalTo(1)))
                .andExpect(jsonPath("$.content.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.content.[0].animalType", equalTo("DOG")))
                .andExpect(jsonPath("$.content.[0].breed", equalTo("cavalier")))
                .andExpect(jsonPath("$.content.[0].animalStatus", equalTo("AVAILABLE")));

    }


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

    List<AnimalOutput> getAnimalsOutput() {
        return Arrays.asList(
                new AnimalOutput(1L, AnimalStatus.AVAILABLE, AnimalType.DOG, "cavalier", timeSupplier.get().minusYears(2), new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
                ), new AnimalOutput(2L, AnimalStatus.AVAILABLE, AnimalType.DOG, "cocker spaniel", timeSupplier.get(), new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
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


}
