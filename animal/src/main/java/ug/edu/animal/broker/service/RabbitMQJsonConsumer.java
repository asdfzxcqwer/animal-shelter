package ug.edu.animal.broker.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ug.edu.animal.animal.persistance.Animal;
import ug.edu.animal.animal.persistance.AnimalRepository;

@Service
public class RabbitMQJsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);
    private final AnimalRepository animalRepository;

    public RabbitMQJsonConsumer(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consumeJsonMessage(Animal animal){
        if(animalRepository.findById(animal.getId()).isPresent()) {
            animalRepository.deleteById(animal.getId());
        }else
        {
            animalRepository.saveAndFlush(animal);
        }
        LOGGER.info(String.format("Received JSON message -> %s", animal.toString()));
    }
}