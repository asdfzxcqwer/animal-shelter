package ug.edu.breeder.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ug.edu.breeder.common.TimeSupplier;

@ControllerAdvice
class ControllerExceptionHandler {
    private final TimeSupplier timeSupplier;

    ControllerExceptionHandler(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> exceptionHandler(LogicException logicException) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                logicException.getMessage(),
                timeSupplier.get()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
    }
}
