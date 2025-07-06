package by.belaya.coworking.service;

import by.belaya.coworking.model.Role;
import by.belaya.coworking.model.UserDTO;
import by.belaya.coworking.service.api.IUserService;
import by.belaya.coworking.service.api.IUserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import by.belaya.coworking.repository.api.IUserRepository;
import by.belaya.coworking.repository.entity.User;

@Service
@Transactional
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidator userValidator;

    public UserService(IUserRepository userRepository, IUserValidator validator) {
        this.userRepository = userRepository;
        this.userValidator = validator;
    }

    @Override
    public User login(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        String rawLogin = userDTO.getLogin();
        if (rawLogin == null || rawLogin.isBlank()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        final String login = rawLogin.trim();
        userValidator.validateLogin(login);

        return userRepository.find(login)
                .orElseGet(() -> addNewUser(login));
    }

    private User addNewUser(String login) {
        Role role = isAdmin(login) ? Role.ADMIN : Role.CUSTOMER;
        User user = new User(role, login);
        userRepository.add(user);
        return user;
    }

    private boolean isAdmin(String login) {
        return "admin".equalsIgnoreCase(login);
    }
}
