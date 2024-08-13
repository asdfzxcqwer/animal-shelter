package ug.edu.breeder.breeder.service;

import ug.edu.breeder.animal.service.serviceimpl.AnimalOutput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederLoginInput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederOutput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederRegistrationInput;

public interface BreederService {
    BreederOutput loginBreeder(BreederLoginInput breederLoginInput);

    BreederOutput registerBreeder(BreederRegistrationInput breederRegistrationInput);

    void deregisterBreeder(Long id);

}
