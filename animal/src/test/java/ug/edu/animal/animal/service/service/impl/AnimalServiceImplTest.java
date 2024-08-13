package ug.edu.animal.animal.service.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ug.edu.animal.animal.exception.AnimalException;
import ug.edu.animal.animal.exception.AnimalExceptionMessage;
import ug.edu.animal.animal.persistance.*;
import ug.edu.animal.animal.service.serviceimpl.AnimalOutput;
import ug.edu.animal.animal.service.serviceimpl.AnimalServiceImpl;
import ug.edu.animal.common.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {
    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    AnimalServiceImpl animalService;
    @Mock
    TimeSupplier timeSupplier;


    @Test
    @DisplayName("Can new animal")
    void canGetAnimal() {
        // given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        // when
        Animal animal = Animal.builder()
                .id(1L)
                .animalType(AnimalType.DOG)
                .breed("Cocker Spaniel")
                .animalStatus(AnimalStatus.AVAILABLE)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();

        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(animal));

        // then
        AnimalOutput result = animalService.getAnimal(1L);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("while getting animal it should throw ANIMAL_NOT_FOUND")
    void shouldThrowANIMAL_NOT_FOUNDException() {

        // then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() -> animalService.getAnimal(1L))
                .withMessage(AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage());
    }
    @Test
    @DisplayName("while getting animals by status it should throw ANIMAL_STATUS_DOES_NOT_EXIST.")
    void shouldThrowANIMAL_STATUS_DOES_NOT_EXISTException() {

        // then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() -> animalService.getAnimalsByStatus("Bad",Pageable.unpaged()))
                .withMessage(AnimalExceptionMessage.ANIMAL_STATUS_DOES_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("Can get all animals")
    void canGetAllAnimals() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        Animal animal = Animal.builder()
                .id(1L)
                .animalType(AnimalType.DOG)
                .breed("Cocker Spaniel")
                .animalStatus(AnimalStatus.AVAILABLE)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();
        when(animalRepository.findByAnimalStatus(AnimalStatus.AVAILABLE,Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(animal)));
        // when
        animalService.getAnimalsByStatus("AVAILABLE", Pageable.unpaged());

        //then
        verify(animalRepository).findByAnimalStatus(AnimalStatus.AVAILABLE,Pageable.unpaged());
    }


}
