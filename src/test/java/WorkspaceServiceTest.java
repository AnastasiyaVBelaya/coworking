import by.belaya.coworking.dto.workspace.WorkspaceCreateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.entity.Workspace;
import by.belaya.coworking.enums.WorkspaceType;
import by.belaya.coworking.exception.WorkspaceNotFoundException;
import by.belaya.coworking.repository.WorkspaceRepository;
import by.belaya.coworking.service.WorkspaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    private WorkspaceServiceImpl workspaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workspaceService = new WorkspaceServiceImpl(workspaceRepository);
    }

    @Test
    void create_ShouldThrowException_WhenCreateDtoIsNull() {
        WorkspaceNotFoundException ex = assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.create(null));
        assertEquals("WorkspaceCreateRequestDto cannot be null", ex.getMessage());
        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void create_ShouldSaveWorkspaceAndReturnDto() {
        WorkspaceCreateRequestDto createDto = new WorkspaceCreateRequestDto(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(1000));
        Workspace workspace = new Workspace(createDto.getType(), createDto.getPrice(), true);

        when(workspaceRepository.save(any(Workspace.class))).thenReturn(workspace);

        WorkspaceResponseDto response = workspaceService.create(createDto);

        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);
        verify(workspaceRepository).save(captor.capture());
        Workspace saved = captor.getValue();

        assertEquals(createDto.getType(), saved.getType());
        assertEquals(createDto.getPrice(), saved.getPrice());
        assertTrue(saved.isAvailable());

        assertNotNull(response);
        assertEquals(workspace.getId(), response.getId());
        assertEquals(saved.getType(), response.getType());
        assertEquals(saved.getPrice(), response.getPrice());
        assertTrue(response.isAvailable());
    }

    @Test
    void getById_ShouldReturnDto_WhenWorkspaceExists() {
        UUID id = UUID.randomUUID();
        Workspace workspace = new Workspace(WorkspaceType.PRIVATE, BigDecimal.valueOf(500), true);

        when(workspaceRepository.findById(id)).thenReturn(Optional.of(workspace));

        WorkspaceResponseDto response = workspaceService.getById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(workspace.getType(), response.getType());
        verify(workspaceRepository).findById(id);
    }

    @Test
    void getById_ShouldThrowException_WhenWorkspaceNotFound() {
        UUID id = UUID.randomUUID();
        when(workspaceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.getById(id));
        verify(workspaceRepository).findById(id);
    }

    @Test
    void getAll_ShouldReturnListOfDtos() {
        Workspace w1 = new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(500), true);
        Workspace w2 = new Workspace(WorkspaceType.ROOM, BigDecimal.valueOf(200), false);

        when(workspaceRepository.findAll()).thenReturn(List.of(w1, w2));

        List<WorkspaceResponseDto> list = workspaceService.getAll();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(dto -> dto.getId().equals(w1.getId())));
        assertTrue(list.stream().anyMatch(dto -> dto.getId().equals(w2.getId())));

        verify(workspaceRepository).findAll();
    }

    @Test
    void getAvailable_ShouldReturnOnlyAvailableDtos() {
        Workspace w1 = new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(500), true);
        Workspace w2 = new Workspace(WorkspaceType.ROOM, BigDecimal.valueOf(200), false);

        when(workspaceRepository.findAll()).thenReturn(List.of(w1, w2));

        List<WorkspaceResponseDto> available = workspaceService.getAvailable();

        assertEquals(1, available.size());
        assertEquals(w1.getId(), available.get(0).getId());
        assertTrue(available.get(0).isAvailable());

        verify(workspaceRepository).findAll();
    }

    @Test
    void update_ShouldThrowException_WhenIdOrDtoIsNull() {
        WorkspaceUpdateRequestDto updateDto = new WorkspaceUpdateRequestDto(WorkspaceType.ROOM, BigDecimal.valueOf(1000), true);

        WorkspaceNotFoundException ex1 = assertThrows(WorkspaceNotFoundException.class,
                () -> workspaceService.update(null, updateDto));
        assertEquals("WorkspaceUpdateRequestDto or ID cannot be null", ex1.getMessage());

        WorkspaceNotFoundException ex2 = assertThrows(WorkspaceNotFoundException.class,
                () -> workspaceService.update(UUID.randomUUID(), null));
        assertEquals("WorkspaceUpdateRequestDto or ID cannot be null", ex2.getMessage());

        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void update_ShouldThrowException_WhenWorkspaceNotFound() {
        UUID id = UUID.randomUUID();
        WorkspaceUpdateRequestDto updateDto = new WorkspaceUpdateRequestDto(WorkspaceType.ROOM, BigDecimal.valueOf(1000), true);

        when(workspaceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.update(id, updateDto));
        verify(workspaceRepository).findById(id);
    }

    @Test
    void update_ShouldModifyWorkspaceAndReturnDto() {
        UUID id = UUID.randomUUID();
        Workspace existing = new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(500), false);

        WorkspaceUpdateRequestDto updateDto = new WorkspaceUpdateRequestDto(WorkspaceType.ROOM, BigDecimal.valueOf(1500), true);

        when(workspaceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(workspaceRepository.save(existing)).thenReturn(existing);

        WorkspaceResponseDto updated = workspaceService.update(id, updateDto);

        assertEquals(updateDto.getType(), updated.getType());
        assertEquals(updateDto.getPrice(), updated.getPrice());
        assertTrue(updated.isAvailable());

        verify(workspaceRepository).findById(id);
        verify(workspaceRepository).save(existing);
    }

    @Test
    void delete_ShouldThrowException_WhenIdIsNull() {
        WorkspaceNotFoundException ex = assertThrows(WorkspaceNotFoundException.class,
                () -> workspaceService.delete(null));
        assertEquals("Workspace ID cannot be null", ex.getMessage());
        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void delete_ShouldThrowException_WhenWorkspaceNotExists() {
        UUID id = UUID.randomUUID();
        when(workspaceRepository.existsById(id)).thenReturn(false);

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.delete(id));
        verify(workspaceRepository).existsById(id);
    }

    @Test
    void delete_ShouldCallDeleteById_WhenWorkspaceExists() {
        UUID id = UUID.randomUUID();
        when(workspaceRepository.existsById(id)).thenReturn(true);
        doNothing().when(workspaceRepository).deleteById(id);

        workspaceService.delete(id);

        verify(workspaceRepository).existsById(id);
        verify(workspaceRepository).deleteById(id);
    }
}
