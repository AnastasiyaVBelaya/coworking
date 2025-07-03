package by.belaya.coworking.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ReservationDTO {
    private UUID workspaceId;
    private String login;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public ReservationDTO() {
    }

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

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
