package by.belaya.coworking.service;

import by.belaya.coworking.dto.user.*;
import by.belaya.coworking.entity.User;
import by.belaya.coworking.enums.Role;
import by.belaya.coworking.exception.UserNotFoundException;
import by.belaya.coworking.repository.UserRepository;
import by.belaya.coworking.service.api.UserService;
import by.belaya.coworking.service.api.UserValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @CachePut(value = "usersCache", key = "#result.id")
    @Override
    public UserResponseDto create(UserRegistrationRequestDto createDto) {
        if (createDto == null) {
            throw new IllegalArgumentException("Registration DTO cannot be null");
        }

        userValidator.validateLogin(createDto.getLogin());
        userValidator.validatePassword(createDto.getPassword());

        if (userRepository.existsByLogin(createDto.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }

        User user = new User(
                createDto.getLogin(),
                passwordEncoder.encode(createDto.getPassword()),
                Role.CUSTOMER
        );
        User savedUser = userRepository.save(user);
        return toResponseDto(savedUser);
    }

    @CachePut(value = "usersCache", key = "#id")
    @Override
    public UserResponseDto update(UUID id, UserUpdateRequestDto updateDto) {
        if (updateDto == null) {
            throw new IllegalArgumentException("Update DTO cannot be null");
        }

        userValidator.validateLogin(updateDto.getLogin());
        userValidator.validatePassword(updateDto.getPassword());

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newLogin = updateDto.getLogin();
        if (newLogin != null && !newLogin.isBlank()) {
            userValidator.validateLogin(newLogin);
            user.setLogin(newLogin);
        }

        String newPassword = updateDto.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            userValidator.validatePassword(newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        User updatedUser = userRepository.save(user);
        return toResponseDto(updatedUser);
    }

    @Caching(evict = {
            @CacheEvict(value = "usersCache", key = "#id"),
            @CacheEvict(value = "usersCache", key = "'all'")
    })
    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Cacheable(value = "usersCache", key = "#id")
    @Override
    public UserResponseDto getById(UUID id) {
        return userRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Cacheable(value = "usersCache", key = "'all'")
    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public User getReferenceById(UUID id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public UserResponseDto login(UserLoginRequestDto loginDto) {
        if (loginDto == null || loginDto.getLogin() == null || loginDto.getPassword() == null) {
            throw new IllegalArgumentException("Login credentials required");
        }

        User user = userRepository.findByLogin(loginDto.getLogin())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getLogin()));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return toResponseDto(user);
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getLogin(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

