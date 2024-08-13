package ug.edu.animal.payment.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ug.edu.animal.payment.persistance.PaymentStatus;
import ug.edu.animal.payment.service.PaymentService;
import ug.edu.animal.payment.service.serviceimpl.PaymentInput;
import ug.edu.animal.payment.service.serviceimpl.PaymentOutput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
class PaymentController {
    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    ResponseEntity<PaymentOutput> createPayment(@RequestBody PaymentInput paymentInput) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.newPayment(paymentInput));
    }
    @GetMapping("/{paymentStatus}")
    ResponseEntity<List<PaymentOutput>> createPayment(@PathVariable PaymentStatus paymentStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.checkPaymentByStatus(paymentStatus));
    }
    @GetMapping("/check/{sessionId}")
    void checkPayment(@PathVariable String sessionId) {
        paymentService.checkAndUpdatePaymentStatus(sessionId);
    }

    @GetMapping( )
    // "2016-03-04 11:30"
    ResponseEntity<List<PaymentOutput>> getPaymentsSince(@RequestParam(value="date")   String since) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");;
        LocalDateTime date = dateTimeFormat.parse(since, LocalDateTime::from);
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentsSince(date));
    }
}
