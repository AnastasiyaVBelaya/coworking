package by.belaya.coworking.repository;

import by.belaya.coworking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Set<Reservation> findByUser_Id(UUID id);

}
