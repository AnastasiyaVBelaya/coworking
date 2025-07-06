package by.belaya.coworking.dto.workspace;

import by.belaya.coworking.enums.WorkspaceType;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class WorkspaceUpdateRequestDto {

    private WorkspaceType type;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Boolean available;

    public WorkspaceUpdateRequestDto() {}

    public WorkspaceUpdateRequestDto(WorkspaceType type, BigDecimal price, Boolean available) {
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public @Positive(message = "Price must be positive") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@Positive(message = "Price must be positive") BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
