package ug.edu.animal.animal.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ug.edu.animal.animal.service.AnimalService;
import ug.edu.animal.animal.service.serviceimpl.AnimalOutput;


import java.util.List;

@RestController
@RequestMapping("api/v1/animals")
class AnimalController {
    private final AnimalService animalService;

    AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }


    @GetMapping("/{id}")
    ResponseEntity<AnimalOutput> getAnimal(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimal(id));
    }


    @GetMapping()
    ResponseEntity<Page<AnimalOutput>> getAnimalsByStatus(@RequestParam String status, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimalsByStatus(status, pageable));
    }

}
