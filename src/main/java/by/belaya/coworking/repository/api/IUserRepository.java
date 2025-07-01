package by.belaya.coworking.repository.api;

import by.belaya.coworking.repository.entity.User;

import java.util.Optional;

public interface IUserRepository {
    User add(User user);

    Optional<User> find(String login);
}
