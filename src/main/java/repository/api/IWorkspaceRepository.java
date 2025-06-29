package repository.api;

import repository.entity.Workspace;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IWorkspaceRepository {
    Workspace add(Workspace workspace);

    Optional<Workspace> find(UUID id);

    Set<Workspace> findAll();

    Set<Workspace> findAvailable();

    Workspace update(Workspace workspace);

    boolean remove(UUID id);
}
