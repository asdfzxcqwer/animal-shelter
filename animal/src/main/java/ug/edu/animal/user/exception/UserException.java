package ug.edu.animal.user.exception;

import ug.edu.animal.exception.LogicException;

public class UserException extends LogicException {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}
