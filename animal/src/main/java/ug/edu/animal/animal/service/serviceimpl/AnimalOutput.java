package ug.edu.animal.animal.service.serviceimpl;

import ug.edu.animal.animal.persistance.AnimalStatus;
import ug.edu.animal.animal.persistance.AnimalType;
import ug.edu.animal.animal.persistance.Price;

import java.time.LocalDateTime;

public record AnimalOutput(
        Long id,
        AnimalStatus animalStatus,
        AnimalType animalType,
        String breed,
        LocalDateTime dateOfBirth,
        Price price
) {
}
