package ug.edu.breeder.animal.exception;

import ug.edu.breeder.security.exception.LogicException;

public class AnimalException extends LogicException {
    public AnimalException(String errorMessage) {
        super(errorMessage);
    }
}
