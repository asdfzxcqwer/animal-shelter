package ug.edu.animal.payment.service.serviceimpl;

import ug.edu.animal.payment.persistance.Payment;

class PaymentMapper {
    public PaymentOutput mapToPaymentOutput(Payment payment,String link,String sessionId) {
        return PaymentOutput.builder()
                .animalId(payment.getAnimalId())
                .city(payment.getCity())
                .email(payment.getEmail())
                .timeBought(payment.getTimeBought())
                .house(payment.getHouse())
                .link(link)
                .sessionId(sessionId)
                .build();
    }

    public Payment mapToPayment(PaymentInput payment) {
        return Payment.builder()
                .animalId(payment.animalId())
                .city(payment.city())
                .house(payment.house())
                .email(payment.email())
                .build();
    }
}
