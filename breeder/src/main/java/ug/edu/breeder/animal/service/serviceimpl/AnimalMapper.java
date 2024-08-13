package ug.edu.breeder.animal.service.serviceimpl;

import ug.edu.breeder.animal.persistance.Animal;

public class AnimalMapper {
    Animal mapToAnimal(AnimalInput animalInput) {
        return Animal.builder()
                .animalType(animalInput.animalType())
                .animalStatus(animalInput.animalStatus())
                .breed(animalInput.breed())
                .breederId(animalInput.breederId())
                .dateOfBirth(animalInput.dateOfBirth())
                .price(animalInput.price())
                .build();
    }

    AnimalOutput mapToAnimalOutput(Animal animal) {
        return new AnimalOutput(animal.getId(),
                animal.getAnimalStatus(),
                animal.getAnimalType(),
                animal.getBreed(),
                animal.getDateOfBirth(),
                animal.getPrice());
    }
}
