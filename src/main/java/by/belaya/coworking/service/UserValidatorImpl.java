package by.belaya.coworking.service;

import by.belaya.coworking.service.api.UserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl implements UserValidator {

    private static final int LOGIN_MIN_LENGTH = 3;
    private static final int LOGIN_MAX_LENGTH = 20;

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 64;

    @Override
    public void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        String trimmed = login.trim();

        if (trimmed.length() < LOGIN_MIN_LENGTH || trimmed.length() > LOGIN_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Login length must be between " + LOGIN_MIN_LENGTH + " and " + LOGIN_MAX_LENGTH + " characters"
            );
        }

        if (!trimmed.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Login must contain only Latin letters, digits, or underscore");
        }
    }

    @Override
    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + PASSWORD_MIN_LENGTH + " characters long"
            );
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must not exceed " + PASSWORD_MAX_LENGTH + " characters"
            );
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
}
