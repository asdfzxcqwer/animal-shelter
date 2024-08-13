package ug.edu.breeder.animal.service.serviceimpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ug.edu.breeder.animal.exception.AnimalException;
import ug.edu.breeder.animal.exception.AnimalExceptionMessage;
import ug.edu.breeder.animal.persistance.Animal;
import ug.edu.breeder.animal.persistance.AnimalRepository;
import ug.edu.breeder.animal.persistance.AnimalStatus;
import ug.edu.breeder.animal.persistance.AnimalType;
import ug.edu.breeder.animal.service.AnimalService;
import ug.edu.breeder.breeder.persistance.BreederRepository;
import ug.edu.breeder.broker.service.RabbitMQJsonProducer;
import ug.edu.breeder.common.TimeSupplier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;
    private final TimeSupplier timeSupplier;
    private final BreederRepository breederRepository;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    public AnimalServiceImpl(AnimalRepository animalRepository,
                             TimeSupplier timeSupplier,
                             BreederRepository breederRepository, RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.breederRepository = breederRepository;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
        this.animalMapper = new AnimalMapper();
        this.animalRepository = animalRepository;
        this.timeSupplier = timeSupplier;
    }

    @Override
    public AnimalOutput createAnimal(AnimalInput animalInput) {
        validateAnimal(animalInput);
        Animal animal = animalMapper.mapToAnimal(animalInput);
        animal.setAnimalStatus(checkAnimalStatus((animal.getAnimalStatus())));
        Animal animalSaved = animalRepository.save(animal);
        rabbitMQJsonProducer.sendJsonMessage(animalSaved);
        return animalMapper.mapToAnimalOutput(animalSaved);
    }

    @Override
    public AnimalOutput updateAnimal(Long id, AnimalInput animalInput) {
        Optional<Animal> presentAnimal = animalRepository.findById(id);
        if (presentAnimal.isPresent()) {
            validateAnimal(animalInput);
            Animal updatedAnimal = animalMapper.mapToAnimal(animalInput);
            Animal animal = presentAnimal.get();
            animal.setAnimalType(updatedAnimal.getAnimalType());
            animal.setAnimalStatus(checkAnimalStatus((updatedAnimal.getAnimalStatus())));
            animal.setBreed(updatedAnimal.getBreed());
            animal.setDateOfBirth(updatedAnimal.getDateOfBirth());
            animal.setBreederId(updatedAnimal.getBreederId());
            animal.setPrice(updatedAnimal.getPrice());
            rabbitMQJsonProducer.sendJsonMessage(animal);
            Animal animalSaved = animalRepository.save(animal);
            rabbitMQJsonProducer.sendJsonMessage(animalSaved);
            return animalMapper.mapToAnimalOutput(animalSaved);
        } else {
            throw new AnimalException(
                    AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage());
        }
    }

    @Override
    public AnimalOutput statusAnimal(Long id) {
        Animal animal = animalRepository.getReferenceById(id);
        if (animal.getId() == id) {
            animal.setAnimalStatus(AnimalStatus.UNAVAILABLE);
            Animal animalSaved = animalRepository.save(animal);
            return animalMapper.mapToAnimalOutput(animalSaved);
        } else {
            throw new AnimalException(
                    AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage());
        }
    }

    @Override
    public AnimalOutput getAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalException(AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage()));
        return animalMapper.mapToAnimalOutput(animal);
    }

    @Override
    public Page<AnimalOutput> getAnimalsByBreederId(Long breederId, Pageable pageable) {
        return Optional.ofNullable(breederId)
                .map(breederId1 -> animalRepository.findByBreederId(breederId1, pageable))
                .orElseThrow(() -> new AnimalException(AnimalExceptionMessage
                        .BREEDER_NOT_PROVIDED.getMessage()))
                .map(animalMapper::mapToAnimalOutput);
    }

    @Override
    public List<AnimalOutput> getAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::mapToAnimalOutput)
                .toList();
    }

    public void deleteAnimal(Long id) {
        boolean exists = animalRepository.existsById(id);
        if (!exists) {
            throw new AnimalException(AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage());
        }
        rabbitMQJsonProducer.sendJsonMessage(animalRepository.findById(id).get());
        animalRepository.deleteById(id);
    }

    private AnimalStatus checkAnimalStatus(AnimalStatus animalStatus) {
        if (!(AnimalStatus.AVAILABLE.equals(animalStatus) ||
                AnimalStatus.UNAVAILABLE.equals(animalStatus))) {
            animalStatus = AnimalStatus.AVAILABLE;
        }
        return animalStatus;
    }

    private void validateAnimal(AnimalInput animalInput) {
        boolean isDateOfBirthInFuture = isDateOfBirthInFuture(animalInput.dateOfBirth());
        boolean isAnimalTooOld = isAnimalTooOld(animalInput.dateOfBirth());
        boolean isBreedNotProvided = isNameNotProvided(animalInput.breed());
        if (isDateOfBirthInFuture) {
            throw new AnimalException(
                    AnimalExceptionMessage.DATE_OF_BIRTH_IS_IN_FUTURE.getMessage());
        }
        if (isAnimalTooOld) {
            throw new AnimalException(
                    AnimalExceptionMessage.I_THINK_YOUR_ANIMAL_COULD_BE_DEAD.getMessage()
            );
        }
        breederRepository.findById(animalInput.breederId())
                .orElseThrow(() -> new AnimalException(
                        AnimalExceptionMessage.BREEDER_NOT_PROVIDED.getMessage()));

        if (isBreedNotProvided) {
            throw new AnimalException(
                    AnimalExceptionMessage.BREED_NOT_PROVIDED.getMessage()
            );
        }
        if (!(AnimalType.CAT.equals(animalInput.animalType()) ||
                AnimalType.DOG.equals(animalInput.animalType()))) {
            throw new AnimalException(
                    AnimalExceptionMessage.ANIMAL_TYPE_NOT_PROVIDED.getMessage()
            );
        }
        if (animalInput.price().getAmount() == null || animalInput.price().getCurrency() == null) {
            throw new AnimalException(
                    AnimalExceptionMessage.PRICE_NOT_PROVIDED.getMessage());
        }


    }

    private boolean isDateOfBirthInFuture(LocalDateTime dateOfBirth) {
        if (dateOfBirth.isAfter(timeSupplier.get())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isAnimalTooOld(LocalDateTime dateOfBirth) {
        if (dateOfBirth.isBefore(timeSupplier.get().minusYears(20))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isNameNotProvided(String breederName) {
        return Optional.ofNullable(breederName)
                .map(String::isBlank)
                .orElse(Boolean.TRUE);
    }
}
