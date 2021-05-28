package com.vojtechruzicka.javafxweaverexample.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SerialNumberValidationService {
    private final static String SERIAL_NUMBER_REGEX =
            "^[0-9]{2}-[0-9]{4}-[0-9]{4}$";

    private final Pattern passwordPattern = Pattern.compile(SERIAL_NUMBER_REGEX);

    public boolean isValidSerialNumber(String password) {
        if (password == null) {
            return false;
        }
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }
}
