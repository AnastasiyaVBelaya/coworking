package repository.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import repository.api.IUserRepository;
import repository.entity.User;

import java.util.Optional;

@Repository
public class HibernateUserRepository implements IUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User add(User user) {
        if (user == null || user.getLogin() == null) {
            throw new IllegalArgumentException("User or login cannot be null");
        }
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> find(String login) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.login) = LOWER(:login)", User.class);
        query.setParameter("login", login);
        return query.getResultStream().findFirst();
    }
}
