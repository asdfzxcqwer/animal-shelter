package ug.edu.animal.payment.service.serviceimpl;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentOutput(
        Long animalId,
        String city,
        String house,
        String email,
        String link,
        String sessionId,
        LocalDateTime timeBought
) {
}
