package by.belaya.coworking.repository;

import by.belaya.coworking.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    Set<Workspace> findByAvailableTrue();
}
