package ug.edu.animal.exception;

import java.time.LocalDateTime;

record ErrorResponse(
        Integer status,
        String message,
        LocalDateTime time
) {
}
