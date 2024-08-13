package ug.edu.animal.exception;

public abstract class LogicException extends RuntimeException {
    protected LogicException(String errorMessage) {
        super(errorMessage);
    }
}