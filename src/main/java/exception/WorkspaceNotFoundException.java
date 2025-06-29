package exception;

import java.util.UUID;

public class WorkspaceNotFoundException extends RuntimeException {
    private final UUID workspaceId;

    public WorkspaceNotFoundException(UUID id) {
        super("Workspace with ID " + id + " not found");
        this.workspaceId = id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }
}
