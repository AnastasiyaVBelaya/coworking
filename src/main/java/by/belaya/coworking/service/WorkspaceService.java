package by.belaya.coworking.service;

import by.belaya.coworking.exception.WorkspaceNotFoundException;
import by.belaya.coworking.model.WorkspaceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import by.belaya.coworking.repository.api.IWorkspaceRepository;
import by.belaya.coworking.repository.entity.Workspace;
import by.belaya.coworking.service.api.IWorkspaceService;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class WorkspaceService implements IWorkspaceService {
    private final IWorkspaceRepository workspaceRepository;

    public WorkspaceService(IWorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace add(WorkspaceDTO workspaceDTO) {
        if (workspaceDTO == null) {
            throw new IllegalArgumentException("Workspace cannot be null!");
        }
        Workspace workspace = new Workspace(workspaceDTO.getType(), workspaceDTO.getPrice(), workspaceDTO.isAvailable());
        return workspaceRepository.add(workspace);
    }

    @Override
    public Workspace find(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Workspace ID cannot be null!");
        }
        return workspaceRepository.find(id).orElseThrow(() ->
                new WorkspaceNotFoundException(id));
    }

    @Override
    public Set<Workspace> findAll() {
        return workspaceRepository.findAll();
    }

    @Override
    public Set<Workspace> findAvailable() {
        return workspaceRepository.findAvailable();
    }

    @Override
    public Workspace update(UUID id, WorkspaceDTO workspaceDTO) {
        if (id == null || workspaceDTO == null) {
            throw new IllegalArgumentException("WorkspaceDTO or ID cannot be null!");
        }

        Workspace existingWorkspace = find(id);
        existingWorkspace.setType(workspaceDTO.getType());
        existingWorkspace.setPrice(workspaceDTO.getPrice());
        existingWorkspace.setAvailable(workspaceDTO.isAvailable());

        return workspaceRepository.update(existingWorkspace);
    }

    @Override
    public boolean remove(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Workspace ID cannot be null!");
        }
        return workspaceRepository.remove(id);
    }
}
