package com.example.testspringpassword;

import com.example.testspringpassword.domain.service.PasswordValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestSpringPasswordApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringPasswordApplication.class, args);
    }

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator();
    }
}