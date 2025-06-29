package repository.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import repository.api.IReservationRepository;
import repository.entity.Reservation;

import java.util.*;

@Repository
public class HibernateReservationRepository implements IReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Reservation add(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Reservation.class, id));
    }

    @Override
    public Set<Reservation> findAll() {
        return new HashSet<>(entityManager
                .createQuery("SELECT r FROM Reservation r", Reservation.class)
                .getResultList());
    }

    @Override
    public Set<Reservation> findByUser(String login) {
        if (login == null || login.trim().isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(entityManager
                .createQuery("SELECT r FROM Reservation r WHERE r.user.login = :login", Reservation.class)
                .setParameter("login", login)
                .getResultList());
    }

    @Override
    public boolean remove(UUID id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation == null) {
            return false;
        }
        entityManager.remove(reservation);
        return true;
    }
}
