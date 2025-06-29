package service;

import org.springframework.stereotype.Component;
import service.api.IUserValidator;

@Component
public class UserValidator implements IUserValidator {
    public void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        if (!login.matches("[a-zA-Z0-9_]{3,20}")) {
            throw new IllegalArgumentException("Login must be 3–20 characters, only latin letters, digits, or underscore");
        }
    }
}
