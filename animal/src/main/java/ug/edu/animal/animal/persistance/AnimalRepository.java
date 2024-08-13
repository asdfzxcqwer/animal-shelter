package ug.edu.animal.animal.persistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Page<Animal> findByAnimalStatus(AnimalStatus animalStatus, Pageable pageable);
}
