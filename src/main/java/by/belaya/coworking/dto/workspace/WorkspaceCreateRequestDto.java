package by.belaya.coworking.dto.workspace;

import by.belaya.coworking.enums.WorkspaceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class WorkspaceCreateRequestDto {

    @NotNull(message = "Workspace type must be specified")
    private WorkspaceType type;

    @NotNull(message = "Price must be specified")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    public WorkspaceCreateRequestDto() {}

    public WorkspaceCreateRequestDto(WorkspaceType type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
