package exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(UUID id) {
        super("Reservation with ID " + id + " not found");
    }
}
