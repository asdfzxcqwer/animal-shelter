package ug.edu.animal.payment.service.serviceimpl;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ug.edu.animal.animal.exception.AnimalException;
import ug.edu.animal.animal.exception.AnimalExceptionMessage;
import ug.edu.animal.animal.persistance.Animal;
import ug.edu.animal.animal.persistance.AnimalRepository;
import ug.edu.animal.animal.persistance.AnimalStatus;
import ug.edu.animal.common.TimeSupplier;
import ug.edu.animal.payment.persistance.Payment;
import ug.edu.animal.payment.persistance.PaymentRepository;
import ug.edu.animal.payment.persistance.PaymentStatus;
import ug.edu.animal.payment.service.PaymentService;
import ug.edu.animal.user.exception.UserException;
import ug.edu.animal.user.exception.UserExceptionMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final AnimalRepository animalRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TimeSupplier timeSupplier;

    public PaymentServiceImpl(AnimalRepository animalRepository, PaymentRepository paymentRepository, TimeSupplier timeSupplier) {
        this.animalRepository = animalRepository;
        this.paymentRepository = paymentRepository;
        this.timeSupplier = timeSupplier;
        this.paymentMapper = new PaymentMapper();
    }

    @Override
    public PaymentOutput newPayment(PaymentInput paymentInput) {
        Animal animal = animalRepository.findById(paymentInput.animalId()).orElseThrow(() -> new AnimalException(
                AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage()
        ));
        if (AnimalStatus.UNAVAILABLE.equals(animal.getAnimalStatus())) {
            throw new ArithmeticException(AnimalExceptionMessage
                    .ANIMAL_IS_NOT_AVAILABLE.getMessage());
        }
        if (!patternMatchesEmail(paymentInput.email())) {
            throw new UserException(UserExceptionMessage.EMAIL_INVALID.getMessage());
        }
        Payment payment = new Payment();
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", animal.getBreed());
        String outputLink = "";
        String sessionId = "";
        try {
            Product product = Product.create(productParams);

            PriceCreateParams priceParams = PriceCreateParams
                    .builder()
                    .setCurrency(animal.getPrice().getCurrency().toString())
                    .setUnitAmount(BigDecimal.valueOf(animal.getPrice().getAmount().doubleValue() * 100).longValue())
                    .setProduct(product.getId())
                    .build();
            Price price = Price.create(priceParams);

            List<Object> animals = new ArrayList<>();
            Map<String, Object> animalToBuy = new HashMap<>();
            animalToBuy.put("price", price.getId());
            animalToBuy.put("quantity", 1);
            animals.add(animalToBuy);
            Map<String, Object> params = new HashMap<>();
            params.put(
                    "success_url",
                    "http://localhost:3000/payment/success"+"?session_id={CHECKOUT_SESSION_ID}"
            );
            params.put(
                    "cancel_url",
                    "http://localhost:3000/payment/cancel"+"?session_id={CHECKOUT_SESSION_ID}"
            );
            params.put("line_items", animals);
            params.put("mode", "payment");
            Session session = Session.create(params);
            sessionId = session.getId();
            outputLink = session.getUrl();
            session.setSuccessUrl("http://localhost:3000/payment/success"+"?session_id={CHECKOUT_SESSION_ID}");
            session.setCancelUrl("http://localhost:3000/payment/cancel"+"?session_id={CHECKOUT_SESSION_ID}");
            payment = paymentMapper.mapToPayment(paymentInput);
            payment.setPaymentStatus(PaymentStatus.UNPAID);
            payment.setId(UUID.randomUUID());
            payment.setPaymentId(session.getId());
            paymentRepository.save(payment);
        } catch (StripeException e) {
            outputLink = "http://localhost:3000/payment/error";
        }
        return paymentMapper.mapToPaymentOutput(payment, outputLink, sessionId);
    }

    @Async
    @Override
    public void checkAndUpdatePaymentStatus(String id) {
        try {
            Session session = Session.retrieve(id);
            if (session.getPaymentStatus().equals("paid")) {
                Payment payment = paymentRepository.findByPaymentId(id);
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setTimeBought(timeSupplier.get());
                paymentRepository.save(payment);
                Animal animal = animalRepository.findById(payment.getAnimalId()).orElseThrow(() -> new AnimalException(
                        AnimalExceptionMessage.ANIMAL_NOT_FOUND.getMessage()
                ));
                animal.setAnimalStatus(AnimalStatus.UNAVAILABLE);
                animalRepository.save(animal);
            } else if (session.getStatus().equals("expired")) {
                Payment payment = paymentRepository.findByPaymentId(id);
                payment.setPaymentStatus(PaymentStatus.CANCELLED);
                paymentRepository.save(payment);

            }
        } catch (StripeException e) {
        }
    }

    @Override
    public List<PaymentOutput> checkPaymentByStatus(PaymentStatus paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus)
                .stream()
                .map(payment -> paymentMapper.mapToPaymentOutput(payment, "", payment.getPaymentId()))
                .toList();
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkPendingPayments() {
        List<Payment> createdPayments = paymentRepository.findByPaymentStatus(PaymentStatus.UNPAID);
        createdPayments.forEach(payment -> checkAndUpdatePaymentStatus(payment.getPaymentId()));
    }

    private static boolean patternMatchesEmail(String emailAddress) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(emailAddress)
                .matches();
    }

    @Override
    public List<PaymentOutput> getPaymentsSince(LocalDateTime since) {
        return paymentRepository.findByTimeBoughtAfter(since)
                .stream()
                .map(payment -> paymentMapper.mapToPaymentOutput(payment, "", payment.getPaymentId()))
                .toList();
    }
}
