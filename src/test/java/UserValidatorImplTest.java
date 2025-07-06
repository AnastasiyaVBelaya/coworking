import by.belaya.coworking.service.UserValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorImplTest {

    private final UserValidatorImpl validator = new UserValidatorImpl();
    private static final String LONG_PASSWORD = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void validateLogin_ShouldThrowException_ForInvalidInput(String invalidLogin) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLogin(invalidLogin));

        assertEquals("Login cannot be empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", "xz"})
    void validateLogin_ShouldThrowException_ForTooShortLogin(String invalidLogin) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLogin(invalidLogin));

        assertTrue(exception.getMessage().contains("Login length must be between"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"thisLoginIsWayTooLong12345", "verylongloginthatexceedslimit"})
    void validateLogin_ShouldThrowException_ForTooLongLogin(String invalidLogin) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLogin(invalidLogin));

        assertTrue(exception.getMessage().contains("Login length must be between"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user!", "юзер", "user@123", "логин", "user$"})
    void validateLogin_ShouldThrowException_ForInvalidCharacters(String invalidLogin) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLogin(invalidLogin));

        assertEquals("Login must contain only Latin letters, digits, or underscore",
                exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user123", "abc_def", "A1_b2_C3", "validLogin"})
    void validateLogin_ShouldNotThrowException_ForValidLogins(String validLogin) {
        assertDoesNotThrow(() -> validator.validateLogin(validLogin));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validatePassword_ShouldThrowException_ForInvalidInput(String invalidPassword) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(invalidPassword));

        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"short1", "1234567", "aaaaaaa"})
    void validatePassword_ShouldThrowException_ForTooShortPassword(String invalidPassword) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(invalidPassword));

        assertTrue(exception.getMessage().contains("Password must be at least"));
    }

    @Test
    void validatePassword_ShouldThrowException_ForTooLongPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(LONG_PASSWORD));

        assertTrue(exception.getMessage().contains("Password must not exceed"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "!@#$%^&*", "1234!@#$"})
    void validatePassword_ShouldThrowException_ForMissingLetters(String invalidPassword) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(invalidPassword));

        assertEquals("Password must contain at least one letter", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "PASSWORD!", "onlyLetters"})
    void validatePassword_ShouldThrowException_ForMissingDigits(String invalidPassword) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(invalidPassword));

        assertEquals("Password must contain at least one digit", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"pass1234", "PASS1234", "onlyLettersAndDigits123"})
    void validatePassword_ShouldThrowException_ForMissingSpecialChars(String invalidPassword) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePassword(invalidPassword));

        assertEquals("Password must contain at least one special character", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Valid1!Pass", "P@ssw0rd123", "A1_b2_C3!Long", "GoodPass123!$"})
    void validatePassword_ShouldNotThrowException_ForValidPasswords(String validPassword) {
        assertDoesNotThrow(() -> validator.validatePassword(validPassword));
    }
}