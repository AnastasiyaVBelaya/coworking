package service.api;

import model.WorkspaceDTO;
import repository.entity.Workspace;

import java.util.Set;
import java.util.UUID;

public interface IWorkspaceService {
    Workspace add(WorkspaceDTO workspaceDTO);

    Workspace find(UUID id);

    Set<Workspace> findAll();

    Set<Workspace> findAvailable();

    Workspace update(UUID id, WorkspaceDTO workspaceDTO);

    boolean remove(UUID id);
}
