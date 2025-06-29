package repository.api;

import repository.entity.Reservation;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IReservationRepository {
    Reservation add(Reservation reservation);

    Optional<Reservation> findById(UUID id);

    Set<Reservation> findAll();

    Set<Reservation> findByUser(String login);

    boolean remove(UUID id);
}
