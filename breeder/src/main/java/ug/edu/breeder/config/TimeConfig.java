package ug.edu.breeder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ug.edu.breeder.common.TimeSupplier;

import java.time.LocalDateTime;

@Configuration
class TimeConfig {
    @Bean
    TimeSupplier timeSupplier() {
        return LocalDateTime::now;
    }
}
