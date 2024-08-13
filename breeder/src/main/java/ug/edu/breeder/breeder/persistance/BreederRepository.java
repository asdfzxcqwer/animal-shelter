package ug.edu.breeder.breeder.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreederRepository extends JpaRepository<Breeder, Long> {
    List<Breeder> findByEmail(String email);
}
