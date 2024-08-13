package ug.edu.animal.animal.service.serviceimpl;

import lombok.Builder;
import ug.edu.animal.animal.persistance.AnimalStatus;
import ug.edu.animal.animal.persistance.AnimalType;
import ug.edu.animal.animal.persistance.Price;

import java.time.LocalDateTime;

@Builder
public record AnimalInput(
        AnimalType animalType,
        AnimalStatus animalStatus,
        String breed,
        LocalDateTime dateOfBirth,
        Long breederId,
        Price price) {
}
