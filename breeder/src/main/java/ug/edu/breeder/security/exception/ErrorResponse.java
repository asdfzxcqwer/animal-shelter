package ug.edu.breeder.security.exception;

import java.time.LocalDateTime;

record ErrorResponse(
        Integer status,
        String message,
        LocalDateTime time
) {
}
