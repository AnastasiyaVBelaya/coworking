package by.belaya.coworking.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationUpdateRequestDto {

    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public ReservationUpdateRequestDto() {}

    public ReservationUpdateRequestDto(LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
