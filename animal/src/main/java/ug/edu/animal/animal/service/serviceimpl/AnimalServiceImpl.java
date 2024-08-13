package ug.edu.animal.animal.service.serviceimpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ug.edu.animal.animal.exception.AnimalException;
import ug.edu.animal.animal.exception.AnimalExceptionMessage;
import ug.edu.animal.animal.persistance.Animal;
import ug.edu.animal.animal.persistance.AnimalRepository;
import ug.edu.animal.animal.persistance.AnimalStatus;
import ug.edu.animal.animal.service.AnimalService;



import java.util.List;
import java.util.Optional;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalMapper = new AnimalMapper();
        this.animalRepository = animalRepository;
    }


    @Override
    public AnimalOutput getAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalException(AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage()));
        return animalMapper.mapToAnimalOutput(animal);
    }


    @Override
    public Page<AnimalOutput> getAnimalsByStatus(String animalStatus, Pageable pageable) {
        return Optional.ofNullable(animalStatus)
                .flatMap(this::getValueOf)
                .map(animalStatus1 -> animalRepository.findByAnimalStatus(animalStatus1, pageable))
                .orElseThrow(() -> new AnimalException(AnimalExceptionMessage
                        .ANIMAL_STATUS_DOES_NOT_EXIST.getMessage()))
                .map(animalMapper::mapToAnimalOutput);
    }

    private Optional<AnimalStatus> getValueOf(String status) {
        if (!AnimalStatus.contains(status)) {
            return Optional.empty();
        }
        return Optional.of(AnimalStatus.valueOf(status));
    }


}
