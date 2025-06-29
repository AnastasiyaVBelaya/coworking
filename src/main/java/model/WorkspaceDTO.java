package model;

import java.math.BigDecimal;

public class WorkspaceDTO {
    private WorkspaceType type;
    private BigDecimal price;
    private boolean available;

    public WorkspaceDTO(WorkspaceType type, BigDecimal price, boolean available) {
        this.type = type;
        this.price = price;
        this.available = available;
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
}
