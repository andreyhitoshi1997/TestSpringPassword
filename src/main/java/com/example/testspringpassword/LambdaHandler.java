package com.example.testspringpassword;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.testspringpassword.application.PasswordValidationService;
import com.example.testspringpassword.domain.model.ValidationResponse;
import com.example.testspringpassword.domain.service.PasswordValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final PasswordValidationService service;
    private final ObjectMapper objectMapper;

    public LambdaHandler() {
        this.service = new PasswordValidationService(new PasswordValidator());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> response = new HashMap<>();
        try {
            String body = (String) input.get("body");
            if (body == null) throw new IllegalArgumentException("Request body is null");
            JsonNode jsonNode = objectMapper.readTree(body);
            String password = jsonNode.get("password").asText();
            if (password == null) throw new IllegalArgumentException("Password field is missing");

            ValidationResponse validation = service.validate(password);
            response.put("statusCode", 200);
            response.put("body", objectMapper.writeValueAsString(validation));
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            response.put("statusCode", 500);
            response.put("body", "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
        response.put("headers", Map.of("Content-Type", "application/json"));
        return response;
    }
}