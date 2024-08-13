package ug.edu.animal.payment.persistance;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    private UUID id;
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @Column(name = "city")
    private String city;
    @Column(name = "house")
    private String house;
    @Column(name = "email")
    private String email;
    @Column(name = "animal_id")
    private Long animalId;
    @Column(name = "time")
    private LocalDateTime timeBought;
}
