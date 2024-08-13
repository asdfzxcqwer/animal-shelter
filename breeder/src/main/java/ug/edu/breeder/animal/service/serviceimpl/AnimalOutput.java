package ug.edu.breeder.animal.service.serviceimpl;

import ug.edu.breeder.animal.persistance.AnimalStatus;
import ug.edu.breeder.animal.persistance.AnimalType;
import ug.edu.breeder.animal.persistance.Price;

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
