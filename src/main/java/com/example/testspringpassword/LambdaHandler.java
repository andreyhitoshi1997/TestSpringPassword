package com.example.testspringpassword;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.testspringpassword.application.PasswordValidationService;
import com.example.testspringpassword.domain.model.ValidationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LambdaHandler implements RequestHandler<String, ValidationResponse> {
    private final PasswordValidationService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LambdaHandler() {
        try {
            var context = new AnnotationConfigApplicationContext(TestSpringPasswordApplication.class);
            this.service = context.getBean(PasswordValidationService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Spring context", e);
        }
    }

    public ValidationResponse handleRequest(String input, Context context) {
        try {
            String cleanInput;
            if (input.startsWith("{")) {
                JsonNode jsonNode = objectMapper.readTree(input);
                cleanInput = jsonNode.get("password").asText();
            } else {
                cleanInput = input.trim().replaceAll("^\"|\"$", "");
            }
            context.getLogger().log("Received input: " + cleanInput);
            return service.validate(cleanInput);
        } catch (Exception e) {
            context.getLogger().log("Error processing input: " + e.getMessage());
            return new ValidationResponse("Internal Server Error: " + e.getMessage(), false);
        }
    }
}