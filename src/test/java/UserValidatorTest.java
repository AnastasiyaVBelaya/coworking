import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import service.UserValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {

    private UserValidator validator = new UserValidator();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void validateLogin_ShouldThrowException_ForNullEmptyOrSpaces(String invalidLogin) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateLogin(invalidLogin));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", "xz"})
    void validateLogin_ShouldThrowException_ForTooShortLogin(String invalidLogin) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateLogin(invalidLogin));
    }

    @ParameterizedTest
    @ValueSource(strings = {"thisLoginIsWayTooLong12345", "verylongloginthatexceedslimit"})
    void validateLogin_ShouldThrowException_ForTooLongLogin(String invalidLogin) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateLogin(invalidLogin));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user!", "юзер", "user@123", "логин", "user$"})
    void validateLogin_ShouldThrowException_ForInvalidCharacters(String invalidLogin) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateLogin(invalidLogin));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user123", "abc_def", "A1_b2_C3", "username_20chars"})
    void validateLogin_ShouldNotThrowException_ForValidLogins(String validLogin) {
        assertDoesNotThrow(() -> validator.validateLogin(validLogin));
    }
}
