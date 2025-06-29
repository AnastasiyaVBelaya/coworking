package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ReservationDTO {
    private final UUID workspaceId;
    private String login;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public ReservationDTO(UUID workspaceId, String login, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.workspaceId = workspaceId;
        this.login = login;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public String getLogin() {
        return login;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
