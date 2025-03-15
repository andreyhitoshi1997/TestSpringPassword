package com.example.testspringpassword.domain.service;

import com.example.testspringpassword.domain.model.Password;

public class PasswordValidator {
    private static final String SPECIAL_CHARS = "!@#$%^&*()-+";

    public boolean isValid(Password password) {
        String value = password.value();
        return hasMinimumLength(value) &&
                hasDigit(value) &&
                hasLowercase(value) &&
                hasUppercase(value) &&
                hasSpecialChar(value);
    }

    private boolean hasMinimumLength(String value) {
        return value.length() >= 9;
    }

    private boolean hasDigit(String value) {
        return value.chars().anyMatch(Character::isDigit);
    }

    private boolean hasLowercase(String value) {
        return value.chars().anyMatch(Character::isLowerCase);
    }

    private boolean hasUppercase(String value) {
        return value.chars().anyMatch(Character::isUpperCase);
    }

    private boolean hasSpecialChar(String value) {
        return value.chars().anyMatch(ch -> SPECIAL_CHARS.indexOf(ch) != -1);
    }
}