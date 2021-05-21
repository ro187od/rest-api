package com.vojtechruzicka.javafxweaverexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final PasswordValidationService passwordValidationService;
    private final LoginValidationService loginValidationService;


    public boolean validateLoginAndPassword(String login, String password) {
        return loginValidationService.isValidLogin(login) &&
                passwordValidationService.isValidPassword(password);
    }

    public boolean validateEmptyLines(String valueOne, String valueTwo) {
        return !valueOne.isEmpty() && !valueTwo.isEmpty();
    }
}
