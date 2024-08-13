package ug.edu.animal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ug.edu.animal.common.TimeSupplier;

import java.time.LocalDateTime;

@Configuration
class TimeConfig {
    @Bean
    TimeSupplier timeSupplier() {
        return LocalDateTime::now;
    }
}
