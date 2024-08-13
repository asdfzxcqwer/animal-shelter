package ug.edu.breeder.animal.service.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ug.edu.breeder.animal.exception.AnimalException;
import ug.edu.breeder.animal.exception.AnimalExceptionMessage;
import ug.edu.breeder.animal.persistance.*;
import ug.edu.breeder.animal.service.serviceimpl.AnimalInput;
import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;
import ug.edu.breeder.animal.service.serviceimpl.AnimalServiceImpl;
import ug.edu.breeder.breeder.persistance.Breeder;
import ug.edu.breeder.breeder.persistance.BreederRepository;
import ug.edu.breeder.broker.service.RabbitMQJsonProducer;
import ug.edu.breeder.common.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {
    @Mock
    AnimalRepository animalRepository;
    @Mock
    BreederRepository breederRepository;
    @InjectMocks
    AnimalServiceImpl animalService;
    @Mock
    TimeSupplier timeSupplier;
    @Mock
    RabbitMQJsonProducer rabbitMQJsonProducer;

    @Test
    @DisplayName("should return animal which was added")
    void shouldReturnAddedAnimal() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = new AnimalInput(
                AnimalType.DOG,
                AnimalStatus.AVAILABLE,
                "cavalier",
                timeSupplier.get().minusYears(2),
                1L,
                new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
        );
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        when(animalRepository.save(any(Animal.class))).thenAnswer(answer -> answer.getArgument(0));
        //when
        AnimalOutput animalOutput = animalService.createAnimal(animalInput);
        //then
        assertThat(animalOutput).isNotNull();
        verify(animalRepository).save(any(Animal.class));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("while adding animal it should throw BREED_NOT_PROVIDED")
    void shouldThrowBREED_NOT_PROVIDEDException(String name) {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = new AnimalInput(
                AnimalType.DOG,
                AnimalStatus.AVAILABLE,
                name,
                timeSupplier.get().minusYears(2),
                1L,
                new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
        );
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.BREED_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }
    @Test
    @DisplayName("while adding animal it should throw PRICE_NOT_PROVIDED")
    void shouldThrowPRICE_NOT_PROVIDEDException() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = new AnimalInput(
                AnimalType.DOG,
                AnimalStatus.AVAILABLE,
                "name",
                timeSupplier.get().minusYears(2),
                1L,
                new Price()
        );
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.PRICE_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while adding animal it should throw ANIMAL_TYPE_NOT_PROVIDED")
    void shouldThrowANIMAL_TYPE_NOT_PROVIDEDException() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.ANIMAL_TYPE_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while adding animal it should throw DATE_OF_BIRTH_IS_IN_FUTURE")
    void shouldThrowDATE_OF_BIRTH_IS_IN_FUTUREException() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().plusYears(2))
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.DATE_OF_BIRTH_IS_IN_FUTURE.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while adding animal it should throw I_THINK_YOUR_ANIMAL_COULD_BE_DEAD")
    void shouldThrowI_THINK_YOUR_ANIMAL_COULD_BE_DEADException() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().minusYears(21))
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.I_THINK_YOUR_ANIMAL_COULD_BE_DEAD.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while adding animal it should throw BREEDER_NOT_PROVIDED")
    void shouldThrowBREEDER_NOT_PROVIDEDException() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .animalType(AnimalType.DOG)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .breederId(1L)
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();

        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.BREEDER_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("should return animal which was updated")
    void shouldReturnUpdatedAnimal() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = new AnimalInput(
                AnimalType.DOG,
                AnimalStatus.AVAILABLE,
                "cavalier",
                timeSupplier.get().minusYears(2),
                1L,
                new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
        );
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        when(animalRepository.save(any(Animal.class))).thenAnswer(answer -> answer.getArgument(0));
        //when
        AnimalOutput animalOutput = animalService.updateAnimal(1L, animalInput);
        //then
        assertThat(animalOutput).isNotNull();
        verify(animalRepository).save(any(Animal.class));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("while adding animal it should throw BREEDER_NOT_PROVIDED")
    void shouldThrowBREEDER_NOT_PROVIDEDException(String name) {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .animalType(AnimalType.DOG)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .breederId(1L)
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();

        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.createAnimal(animalInput))
                .withMessage(AnimalExceptionMessage.BREEDER_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

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
    @DisplayName("Can get all animals")
    void canGetAllAnimals() {
        // when
        animalService.getAnimals();

        //then
        verify(animalRepository).findAll();
    }

    @Test
    @DisplayName("Can delete animal")
    void canDeleteAnimal() {
        // given
        long id = 10;
        when(animalRepository.existsById(id)).thenReturn(true);
        when(animalRepository.findById(id)).thenReturn(Optional.ofNullable(Animal.builder()
                        .id(10L)
                        .build()));
        // when
        animalService.deleteAnimal(id);

        // then
        verify(animalRepository).deleteById(id);
    }

    @Test
    @DisplayName("while deleting animal it should throw ANIMAL_NOT_FOUND")
    void deleteShouldThrowANIMAL_NOT_FOUNDException() {
        // given
        long id = 10;
        when(animalRepository.existsById(id)).thenReturn(false);

        // when
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() -> animalService.deleteAnimal(id))
                .withMessage(AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage());

        // then
        verify(animalRepository, never()).deleteById(id);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("while updating animal it should throw BREED_NOT_PROVIDED")
    void shouldThrowBREED_NOT_PROVIDEDExceptionWhileUpdating(String name) {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = new AnimalInput(
                AnimalType.DOG,
                AnimalStatus.AVAILABLE,
                name,
                timeSupplier.get().minusYears(2),
                1L,
                new Price(BigDecimal.TEN, Currency.getInstance("PLN"))
        );
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.updateAnimal(1L, animalInput))
                .withMessage(AnimalExceptionMessage.BREED_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while updating animal it should throw ANIMAL_TYPE_NOT_PROVIDED")
    void shouldThrowANIMAL_TYPE_NOT_PROVIDEDExceptionWhileUpdating() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .build();
        when(breederRepository.findById(1L)).thenReturn(Optional.ofNullable(Breeder.builder()
                .id(1L)
                .build()));
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.updateAnimal(1L, animalInput))
                .withMessage(AnimalExceptionMessage.ANIMAL_TYPE_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while updating animal it should throw DATE_OF_BIRTH_IS_IN_FUTURE")
    void shouldThrowDATE_OF_BIRTH_IS_IN_FUTUREExceptionWhileUpdating() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().plusYears(2))
                .build();
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.updateAnimal(1L, animalInput))
                .withMessage(AnimalExceptionMessage.DATE_OF_BIRTH_IS_IN_FUTURE.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while updating animal it should throw I_THINK_YOUR_ANIMAL_COULD_BE_DEAD")
    void shouldThrowI_THINK_YOUR_ANIMAL_COULD_BE_DEADExceptionWhileUpdating() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .breederId(1L)
                .dateOfBirth(timeSupplier.get().minusYears(21))
                .build();
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.updateAnimal(1L, animalInput))
                .withMessage(AnimalExceptionMessage.I_THINK_YOUR_ANIMAL_COULD_BE_DEAD.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    @DisplayName("while updating animal it should throw BREEDER_NOT_PROVIDED")
    void shouldThrowBREEDER_NOT_PROVIDEDExceptionWhileUpdating() {
        //given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2020, JANUARY, 1, 1, 1, 1, 0));
        AnimalInput animalInput = AnimalInput.builder()
                .animalStatus(AnimalStatus.AVAILABLE)
                .breed("cavalier")
                .animalType(AnimalType.DOG)
                .dateOfBirth(timeSupplier.get().minusYears(2))
                .breederId(1L)
                .price(new Price(BigDecimal.TEN, Currency.getInstance("PLN")))
                .build();
        when(animalRepository.findById(1L)).thenReturn(Optional.ofNullable(Animal.builder()
                .id(1L)
                .build()));
        //then
        assertThatExceptionOfType(AnimalException.class)
                .isThrownBy(() ->
                        animalService.updateAnimal(1L, animalInput))
                .withMessage(AnimalExceptionMessage.BREEDER_NOT_PROVIDED.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }
}
