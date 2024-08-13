package ug.edu.breeder.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ug.edu.breeder.common.TimeSupplier;

import java.time.LocalDateTime;

@TestConfiguration
class TimeConfig {
    @Bean
    public TimeSupplier timeSupplier() {
        return () -> LocalDateTime.of(2020, 1, 3, 10, 20, 3);
    }
}

