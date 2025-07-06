package by.belaya.coworking.dto.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ReservationCreateRequestDto {

    @NotNull(message = "User ID must be specified")
    private UUID userId;

    @NotNull(message = "Workspace ID must be specified")
    private UUID workspaceId;

    @NotNull(message = "Reservation date must be specified")
    private LocalDate reservationDate;

    @NotNull(message = "Start time must be specified")
    private LocalTime startTime;

    @NotNull(message = "End time must be specified")
    private LocalTime endTime;

    public ReservationCreateRequestDto() {}

    public ReservationCreateRequestDto(UUID userId, UUID workspaceId, LocalDate reservationDate, LocalTime startTime,
                                       LocalTime endTime) {
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
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
