package by.belaya.coworking.model;

import java.math.BigDecimal;

public class WorkspaceDTO {
    private WorkspaceType type;
    private BigDecimal price;
    private boolean available;

    public WorkspaceDTO() {
    }

    public WorkspaceDTO(WorkspaceType type, BigDecimal price, boolean available) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
