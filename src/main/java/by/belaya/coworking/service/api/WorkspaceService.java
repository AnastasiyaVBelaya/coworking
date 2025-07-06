package by.belaya.coworking.service.api;

import by.belaya.coworking.dto.workspace.WorkspaceCreateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.entity.User;
import by.belaya.coworking.entity.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceResponseDto create(WorkspaceCreateRequestDto createDto);

    WorkspaceResponseDto update(UUID id, WorkspaceUpdateRequestDto updateDto);

    WorkspaceResponseDto getById(UUID id);

    List<WorkspaceResponseDto> getAll();

    List<WorkspaceResponseDto> getAvailable();

    void delete(UUID id);

}
