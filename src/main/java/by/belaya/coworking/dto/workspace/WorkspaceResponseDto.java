package by.belaya.coworking.dto.workspace;

import by.belaya.coworking.enums.WorkspaceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class WorkspaceResponseDto {
    private final UUID id;
    private final WorkspaceType type;
    private final BigDecimal price;
    private final boolean available;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long version;

    public WorkspaceResponseDto(UUID id, WorkspaceType type, BigDecimal price, boolean available,
                                LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public WorkspaceType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}
