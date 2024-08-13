package ug.edu.breeder.animal.persistance;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
@Table(name = "animals")
public class Animal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type")
    private AnimalType animalType;
    @Enumerated(EnumType.STRING)
    @Column(name = "animal_status")
    private AnimalStatus animalStatus;
    @Column(name = "breed")
    private String breed;
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    @Embedded
    private Price price;
    @Column(name = "breeder_id")
    private Long breederId;
}
