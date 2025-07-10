import by.belaya.coworking.dto.user.*;
import by.belaya.coworking.entity.User;
import by.belaya.coworking.enums.Role;
import by.belaya.coworking.repository.UserRepository;
import by.belaya.coworking.service.UserServiceImpl;
import by.belaya.coworking.service.api.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private final UUID TEST_USER_ID = UUID.randomUUID();
    private final String TEST_LOGIN = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final Role TEST_ROLE = Role.CUSTOMER;

    private User createTestUser() {
        User user = new User(TEST_LOGIN, TEST_PASSWORD, TEST_ROLE);
        return user;
    }

    @Test
    void create_ShouldSuccessfullyCreateUser() {
        UserRegistrationRequestDto createDto = new UserRegistrationRequestDto(TEST_LOGIN, TEST_PASSWORD);
        User user = createTestUser();

        when(userRepository.existsByLogin(TEST_LOGIN)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.create(createDto);

        assertNotNull(result);
        assertEquals(TEST_LOGIN, result.getLogin());
        assertEquals(TEST_ROLE, result.getRole());
        verify(userValidator).validateLogin(TEST_LOGIN);
        verify(userValidator).validatePassword(TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        UserLoginRequestDto loginDto = new UserLoginRequestDto(TEST_LOGIN, TEST_PASSWORD);
        User user = createTestUser();

        when(userRepository.findByLogin(TEST_LOGIN)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.login(loginDto);

        assertNotNull(result);
        assertEquals(TEST_LOGIN, result.getLogin());
        assertEquals(TEST_ROLE, result.getRole());
    }

    @Test
    void getById_ShouldReturnUser_WhenExists() {
        User user = new User(TEST_LOGIN, TEST_PASSWORD, TEST_ROLE);
        UUID actualUserId = user.getId();

        when(userRepository.findById(actualUserId)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getById(actualUserId);

        assertNotNull(result);
        assertEquals(actualUserId, result.getId());
        assertEquals(TEST_LOGIN, result.getLogin());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getById(TEST_USER_ID));
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        User user = createTestUser();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDto> result = userService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(TEST_LOGIN, result.get(0).getLogin());
    }

    @Test
    void update_ShouldUpdateUser_WhenExists() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setLogin("new@example.com");
        updateDto.setPassword("newpassword");

        User existingUser = createTestUser();

        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDto result = userService.update(TEST_USER_ID, updateDto);

        assertNotNull(result);
        verify(userValidator, times(2)).validateLogin("new@example.com");
        verify(userValidator, times(2)).validatePassword("newpassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void delete_ShouldDeleteUser_WhenExists() {
        when(userRepository.existsById(TEST_USER_ID)).thenReturn(true);
        userService.delete(TEST_USER_ID);
        verify(userRepository).deleteById(TEST_USER_ID);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(userRepository.existsById(TEST_USER_ID)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.delete(TEST_USER_ID));
    }

    @Test
    void create_ShouldThrowException_WhenLoginExists() {
        UserRegistrationRequestDto createDto = new UserRegistrationRequestDto(TEST_LOGIN, TEST_PASSWORD);
        when(userRepository.existsByLogin(TEST_LOGIN)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.create(createDto));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        UserLoginRequestDto loginDto = new UserLoginRequestDto(TEST_LOGIN, "wrongpassword");
        User user = createTestUser();

        when(userRepository.findByLogin(TEST_LOGIN)).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.login(loginDto));
    }
}