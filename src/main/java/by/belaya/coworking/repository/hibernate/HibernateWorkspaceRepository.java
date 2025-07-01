package by.belaya.coworking.repository.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import by.belaya.coworking.repository.api.IWorkspaceRepository;
import by.belaya.coworking.repository.entity.Workspace;

import java.util.*;

@Repository
public class HibernateWorkspaceRepository implements IWorkspaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Workspace add(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace cannot be null");
        }
        entityManager.persist(workspace);
        return workspace;
    }

    @Override
    public Optional<Workspace> find(UUID id) {
        return Optional.ofNullable(entityManager.find(Workspace.class, id));
    }

    @Override
    public Set<Workspace> findAll() {
        return new HashSet<>(entityManager
                .createQuery("SELECT w FROM Workspace w", Workspace.class)
                .getResultList());
    }

    @Override
    public Set<Workspace> findAvailable() {
        return new HashSet<>(entityManager
                .createQuery("SELECT w FROM Workspace w WHERE w.available = true", Workspace.class)
                .getResultList());
    }

    @Override
    public Workspace update(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace cannot be null");
        }
        return entityManager.merge(workspace);
    }

    @Override
    public boolean remove(UUID id) {
        Workspace workspace = entityManager.find(Workspace.class, id);
        if (workspace == null) {
            return false;
        }
        entityManager.remove(workspace);
        return true;
    }
}
