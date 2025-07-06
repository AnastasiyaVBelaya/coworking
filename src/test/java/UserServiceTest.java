import by.belaya.coworking.model.Role;
import by.belaya.coworking.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import by.belaya.coworking.repository.api.IUserRepository;
import by.belaya.coworking.repository.entity.User;
import by.belaya.coworking.service.UserService;
import by.belaya.coworking.service.api.IUserValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserValidator userValidator;

    @InjectMocks
    private UserService userService;

    @Test
    void login_ShouldThrowException_WhenUserDtoIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.login(null));
        assertEquals("UserDTO cannot be null", ex.getMessage());
        verifyNoInteractions(userValidator, userRepository);
    }

    @Test
    void login_ShouldTrimLogin_AndCallValidatorWithTrimmedLogin() {
        UserDTO dto = new UserDTO("  user1  ");
        when(userRepository.find("user1")).thenReturn(Optional.empty());

        userService.login(dto);

        verify(userValidator).validateLogin("user1");
        verify(userRepository).find("user1");
    }

    @Test
    void login_ShouldReturnExistingUser_WhenUserExists() {
        User existingUser = new User(Role.CUSTOMER, "user1");
        UserDTO dto = new UserDTO("user1");
        when(userRepository.find("user1")).thenReturn(Optional.of(existingUser));

        User actualUser = userService.login(dto);

        assertAll("Verify existing user returned and dependencies called",
                () -> assertSame(existingUser, actualUser),
                () -> verify(userValidator).validateLogin("user1"),
                () -> verify(userRepository).find("user1"),
                () -> verify(userRepository, never()).add(any())
        );
    }

    @Test
    void login_ShouldCreateNewAdminUser_WhenLoginIsAdmin() {
        UserDTO dto = new UserDTO("admin");
        when(userRepository.find("admin")).thenReturn(Optional.empty());

        User user = userService.login(dto);

        assertAll("Verify admin user creation",
                () -> assertEquals(Role.ADMIN, user.getRole()),
                () -> assertEquals("admin", user.getLogin()),
                () -> verify(userValidator).validateLogin("admin"),
                () -> verify(userRepository).find("admin"),
                () -> verify(userRepository).add(user)
        );
    }

    @Test
    void login_ShouldCreateNewCustomerUser_WhenLoginIsNotAdmin() {
        UserDTO dto = new UserDTO("regularUser");
        when(userRepository.find("regularUser")).thenReturn(Optional.empty());

        User user = userService.login(dto);

        assertAll("Verify customer user creation",
                () -> assertEquals(Role.CUSTOMER, user.getRole()),
                () -> assertEquals("regularUser", user.getLogin()),
                () -> verify(userValidator).validateLogin("regularUser"),
                () -> verify(userRepository).find("regularUser"),
                () -> verify(userRepository).add(user)
        );
    }

    @Test
    void login_ShouldPassCorrectUserToAddMethod_WhenCreatingNewUser() {
        UserDTO dto = new UserDTO("admin");
        when(userRepository.find("admin")).thenReturn(Optional.empty());

        userService.login(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).add(userCaptor.capture());

        User addedUser = userCaptor.getValue();

        assertAll("Verify user added has correct login and role",
                () -> assertEquals("admin", addedUser.getLogin()),
                () -> assertEquals(Role.ADMIN, addedUser.getRole())
        );
    }
}
