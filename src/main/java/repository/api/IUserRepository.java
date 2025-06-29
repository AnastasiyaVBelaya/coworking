package repository.api;

import repository.entity.User;

import java.util.Optional;

public interface IUserRepository {
    User add(User user);

    Optional<User> find(String login);
}
