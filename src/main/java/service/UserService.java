package service;

import model.Role;
import model.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.api.IUserRepository;
import repository.entity.User;
import service.api.IUserService;
import service.api.IUserValidator;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidator userValidator;

    public UserService(IUserRepository userRepository, IUserValidator validator) {
        this.userRepository = userRepository;
        this.userValidator = validator;
    }

    @Override
    @Transactional(readOnly = true)
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

    @Transactional
    protected User addNewUser(String login) {
        Role role = isAdmin(login) ? Role.ADMIN : Role.CUSTOMER;
        User user = new User(role, login);
        userRepository.add(user);
        return user;
    }

    private boolean isAdmin(String login) {
        return "admin".equalsIgnoreCase(login);
    }
}
