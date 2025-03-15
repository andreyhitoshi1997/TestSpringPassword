package com.example.testspringpassword.infrastructure.controller;

import com.example.testspringpassword.application.PasswordValidationService;
import com.example.testspringpassword.domain.model.ValidationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validate")
public class PasswordController {
    private final PasswordValidationService service;

    public PasswordController(PasswordValidationService service) {
        this.service = service;
    }

    @PostMapping
    public ValidationResponse validatePassword(@RequestBody String password) {
        return service.validate(password);
    }
}