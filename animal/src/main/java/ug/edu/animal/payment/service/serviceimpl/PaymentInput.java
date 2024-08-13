package ug.edu.animal.payment.service.serviceimpl;

import lombok.Builder;

@Builder
public record PaymentInput(
        Long animalId,
        String city,
        String house,
        String email
) {
}
