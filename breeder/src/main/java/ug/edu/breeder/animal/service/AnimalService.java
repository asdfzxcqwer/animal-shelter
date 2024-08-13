package ug.edu.breeder.animal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ug.edu.breeder.animal.service.serviceimpl.AnimalInput;
import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;

import java.util.List;

public interface AnimalService {
    AnimalOutput createAnimal(AnimalInput animalInput);
    AnimalOutput getAnimal(Long id);

    List<AnimalOutput> getAnimals();

    Page<AnimalOutput> getAnimalsByBreederId(Long breederId, Pageable pageable);

    void deleteAnimal(Long id);


    AnimalOutput updateAnimal(Long id, AnimalInput AnimalInput);

    AnimalOutput statusAnimal(Long id);
}
