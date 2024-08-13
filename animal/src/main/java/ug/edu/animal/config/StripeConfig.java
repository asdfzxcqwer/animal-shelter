package ug.edu.animal.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StripeConfig {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Bean
    StripeSecretKeySupplier stripeSecretKeySupplier() {
        Stripe.apiKey = secretKey;
        return () -> secretKey;
    }
}