package service.api;

import model.ReservationDTO;
import model.UserDTO;
import repository.entity.Reservation;

import java.util.Set;
import java.util.UUID;

public interface IReservationService {
    Reservation add(ReservationDTO reservationDTO, UserDTO userDTO);

    Set<Reservation> findByUser(UserDTO userDTO);

    Set<Reservation> findAll();

    boolean remove(UUID id);
}
