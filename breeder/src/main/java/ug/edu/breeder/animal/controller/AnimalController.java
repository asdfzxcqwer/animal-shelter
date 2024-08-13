package ug.edu.breeder.animal.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ug.edu.breeder.animal.persistance.Animal;
import ug.edu.breeder.animal.service.AnimalService;
import ug.edu.breeder.animal.service.serviceimpl.AnimalInput;
import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;

@RestController
@RequestMapping("api/v1/animals")
class AnimalController {
    private final AnimalService animalService;

    AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping()
    ResponseEntity<AnimalOutput> createAnimal(@RequestBody AnimalInput animalInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.createAnimal(animalInput));
    }

    @GetMapping("/{id}")
    ResponseEntity<AnimalOutput> getAnimal(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimal(id));
    }

    // @GetMapping()
    // ResponseEntity<List<AnimalOutput>> getAnimals(){
    //     return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimals());
    // }
    @GetMapping()
    ResponseEntity<Page<AnimalOutput>> getAnimalsByBreederId(@RequestParam Long breederId, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimalsByBreederId(breederId, pageable));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Animal> deleteAnimal(@PathVariable("id") Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}")
    ResponseEntity<AnimalOutput> changeStatusAnimal(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.statusAnimal(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<AnimalOutput> updateAnimal(@PathVariable("id") Long id, @RequestBody AnimalInput animalInput) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.updateAnimal(id, animalInput));
    }
}
