package com.example.testspringpassword.application;

import com.example.testspringpassword.domain.model.Password;
import com.example.testspringpassword.domain.model.ValidationResponse;
import com.example.testspringpassword.domain.service.PasswordValidator;

public class PasswordValidationService {
    private final PasswordValidator validator;

    public PasswordValidationService(PasswordValidator validator) {
        this.validator = validator;
    }

    public ValidationResponse validate(String password) {
        boolean isValid = validator.isValid(new Password(password));
        return new ValidationResponse(
                isValid ? "Password is valid." : "Password does not meet requirements.",
                isValid
        );
    }
}