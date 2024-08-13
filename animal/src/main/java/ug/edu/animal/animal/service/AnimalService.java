package ug.edu.animal.animal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ug.edu.animal.animal.service.serviceimpl.AnimalOutput;

import java.util.List;

public interface AnimalService {
    AnimalOutput getAnimal(Long id);


    Page<AnimalOutput> getAnimalsByStatus(String animalStatus, Pageable pageable);
}
