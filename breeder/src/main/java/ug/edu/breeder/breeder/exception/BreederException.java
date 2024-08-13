package ug.edu.breeder.breeder.exception;

import ug.edu.breeder.security.exception.LogicException;

public class BreederException extends LogicException {
    public BreederException(String errorMessage) {
        super(errorMessage);
    }
}
