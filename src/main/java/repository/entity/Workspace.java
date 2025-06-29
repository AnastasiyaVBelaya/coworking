package repository.entity;

import jakarta.persistence.*;
import model.WorkspaceType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "workspaces")
public class Workspace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private WorkspaceType type;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "available", nullable = false)
    private boolean available;

    protected Workspace() {
    }

    public Workspace(WorkspaceType type, BigDecimal price, boolean available) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public Workspace(UUID id, WorkspaceType type, BigDecimal price, boolean available) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workspace)) return false;
        Workspace that = (Workspace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Workspace{id=%s, type=%s, price=%s, available=%b}", id, type, price, available);
    }
}
