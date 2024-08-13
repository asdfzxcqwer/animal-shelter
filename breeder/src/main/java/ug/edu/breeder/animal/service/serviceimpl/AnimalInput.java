package ug.edu.breeder.animal.service.serviceimpl;

import lombok.Builder;
import ug.edu.breeder.animal.persistance.AnimalStatus;
import ug.edu.breeder.animal.persistance.AnimalType;
import ug.edu.breeder.animal.persistance.Price;

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
