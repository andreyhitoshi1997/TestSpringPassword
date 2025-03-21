package com.example.testspringpassword.infrastructure;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.testspringpassword.application.PasswordValidationService;
import com.example.testspringpassword.domain.model.ValidationResponse;
import com.example.testspringpassword.domain.service.PasswordValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final PasswordValidationService service;
    private final ObjectMapper objectMapper;

    public LambdaHandler() {
        this.service = new PasswordValidationService(new PasswordValidator());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
            Map<String, Object> response = new HashMap<>();
            try {
                String body = (String) input.get("body");
                if (body == null) {
                    response.put("statusCode", 400);
                    response.put("body", "{\"message\": \"Password does not meet requirements.\"}");
                    return response;
                }

                JsonNode jsonNode = objectMapper.readTree(body);
                String passwordValue = jsonNode.get("password").asText();
                if (passwordValue == null) {
                    response.put("statusCode", 400);
                    response.put("body", "{\"message\": \"Password field is missing\"}");
                    return response;
                }

                ValidationResponse validation = service.validate(passwordValue);
                if (!validation.isValid()) {
                    response.put("statusCode", 400);
                    response.put("body", objectMapper.writeValueAsString(validation));
                    return response;
                }

                response.put("statusCode", 200);
                response.put("body", objectMapper.writeValueAsString(validation));
            } catch (Exception e) {
                context.getLogger().log("Error: " + e.getMessage());
                response.put("statusCode", 500);
                response.put("body", "{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
            response.put("headers", Map.of("Content-Type", "application/json"));
            return response;
        });

        return future.join();
    }
}