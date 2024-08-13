package ug.edu.animal.payment.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    Payment findByPaymentId(String paymentId);
    List<Payment> findByTimeBoughtAfter(LocalDateTime after);
}
