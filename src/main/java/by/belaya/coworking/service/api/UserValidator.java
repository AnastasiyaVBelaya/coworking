package by.belaya.coworking.service.api;

public interface UserValidator {
    void validateLogin(String login);

    void validatePassword(String password);
}
