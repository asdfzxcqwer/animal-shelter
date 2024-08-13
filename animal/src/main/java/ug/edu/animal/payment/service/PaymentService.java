package ug.edu.animal.payment.service;

import ug.edu.animal.payment.persistance.PaymentStatus;
import ug.edu.animal.payment.service.serviceimpl.PaymentInput;
import ug.edu.animal.payment.service.serviceimpl.PaymentOutput;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    PaymentOutput newPayment(PaymentInput paymentInput);
    void checkAndUpdatePaymentStatus(String id);

    List<PaymentOutput> checkPaymentByStatus(PaymentStatus paymentStatus);

    List<PaymentOutput> getPaymentsSince(LocalDateTime since);
}
