package by.belaya.coworking.service;

import by.belaya.coworking.dto.workspace.WorkspaceCreateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.entity.Workspace;
import by.belaya.coworking.exception.WorkspaceNotFoundException;
import by.belaya.coworking.repository.WorkspaceRepository;
import by.belaya.coworking.service.api.WorkspaceService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @CachePut(value = "workspacesCache", key = "#result.id")
    @Caching(evict = {
            @CacheEvict(value = "workspacesCache", key = "'all'"),
            @CacheEvict(value = "workspacesCache", key = "'available'")
    })
    @Override
    public WorkspaceResponseDto create(WorkspaceCreateRequestDto createDto) {
        Workspace workspace = new Workspace(
                createDto.getType(),
                createDto.getPrice(),
                true
        );
        Workspace saved = workspaceRepository.save(workspace);
        return toResponseDto(saved);
    }

    @CachePut(value = "workspacesCache", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "workspacesCache", key = "'all'"),
            @CacheEvict(value = "workspacesCache", key = "'available'")
    })
    @Override
    public WorkspaceResponseDto update(UUID id, WorkspaceUpdateRequestDto updateDto) {
        Workspace existing = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(id));

        existing.setType(updateDto.getType());
        existing.setPrice(updateDto.getPrice());
        existing.setAvailable(updateDto.getAvailable());

        Workspace updated = workspaceRepository.save(existing);
        return toResponseDto(updated);
    }

    @Cacheable(value = "workspacesCache", key = "#id")
    @Override
    public WorkspaceResponseDto getById(UUID id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(id));
        return toResponseDto(workspace);
    }

    @Cacheable(value = "workspacesCache", key = "'all'")
    @Override
    public List<WorkspaceResponseDto> getAll() {
        return workspaceRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "workspacesCache", key = "'available'")
    @Override
    public List<WorkspaceResponseDto> getAvailable() {
        return workspaceRepository.findAll()
                .stream()
                .filter(Workspace::isAvailable)
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public Workspace getReferenceById(UUID id) {
        return workspaceRepository.getReferenceById(id);
    }

    @CacheEvict(value = "workspacesCache", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "workspacesCache", key = "'all'"),
            @CacheEvict(value = "workspacesCache", key = "'available'")
    })
    @Override
    public void delete(UUID id) {
        if (!workspaceRepository.existsById(id)) {
            throw new WorkspaceNotFoundException(id);
        }
        workspaceRepository.deleteById(id);
    }

    private WorkspaceResponseDto toResponseDto(Workspace workspace) {
        return new WorkspaceResponseDto(
                workspace.getId(),
                workspace.getType(),
                workspace.getPrice(),
                workspace.isAvailable(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt(),
                workspace.getVersion()
        );
    }
}
