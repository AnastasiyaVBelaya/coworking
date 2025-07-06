package by.belaya.coworking.service.api;

import by.belaya.coworking.model.ReservationDTO;
import by.belaya.coworking.model.UserDTO;
import by.belaya.coworking.repository.entity.Reservation;

import java.util.Set;
import java.util.UUID;

public interface IReservationService {
    Reservation add(ReservationDTO reservationDTO, UserDTO userDTO);

    Set<Reservation> findByUser(UserDTO userDTO);

    Set<Reservation> findAll();

    boolean remove(UUID id);
}
