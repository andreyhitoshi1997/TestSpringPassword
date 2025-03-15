package com.example.testspringpassword;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.testspringpassword.application.PasswordValidationService;
import com.example.testspringpassword.domain.model.ValidationResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LambdaHandler implements RequestHandler<String, ValidationResponse> {
    private final PasswordValidationService service;

    public LambdaHandler() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringPasswordApplication.class);
        this.service = context.getBean(PasswordValidationService.class);
    }

    @Override
    public ValidationResponse handleRequest(String input, Context context) {
        return service.validate(input);
    }
}