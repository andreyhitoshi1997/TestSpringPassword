package com.example.testspringpassword.domain.model;

public record ValidationResponse(String message, boolean valid) {
    public boolean isValid() {
        return valid;
    }
}