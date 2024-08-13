package ug.edu.breeder.security.exception;

public abstract class LogicException extends RuntimeException {
    protected LogicException(String errorMessage) {
        super(errorMessage);
    }
}
