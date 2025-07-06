import by.belaya.coworking.exception.WorkspaceNotFoundException;
import by.belaya.coworking.model.WorkspaceDTO;
import by.belaya.coworking.model.WorkspaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import by.belaya.coworking.repository.api.IWorkspaceRepository;
import by.belaya.coworking.repository.entity.Workspace;
import by.belaya.coworking.service.WorkspaceService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceTest {

    @Mock
    private IWorkspaceRepository workspaceRepository;

    private WorkspaceService workspaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workspaceService = new WorkspaceService(workspaceRepository);
    }

    @Test
    void add_ShouldThrowException_WhenWorkspaceDtoIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> workspaceService.add(null));
        assertEquals("Workspace cannot be null!", ex.getMessage());
        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void add_ShouldCreateAndAddWorkspace() {
        WorkspaceDTO dto = new WorkspaceDTO(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(1000.0), true);
        Workspace workspace = new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(1000.0), true);

        when(workspaceRepository.add(any())).thenReturn(workspace);

        Workspace result = workspaceService.add(dto);

        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);
        verify(workspaceRepository).add(captor.capture());

        Workspace captured = captor.getValue();
        assertAll("Workspace fields",
                () -> assertEquals(dto.getType(), captured.getType()),
                () -> assertEquals(dto.getPrice(), captured.getPrice()),
                () -> assertEquals(dto.isAvailable(), captured.isAvailable())
        );

        assertSame(workspace, result);
    }

    @Test
    void find_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> workspaceService.find(null));
        assertEquals("Workspace ID cannot be null!", ex.getMessage());
        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void find_ShouldThrowWorkspaceNotFoundException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(workspaceRepository.find(id)).thenReturn(Optional.empty());

        WorkspaceNotFoundException ex = assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.find(id));
        assertEquals(id, ex.getWorkspaceId());
        verify(workspaceRepository).find(id);
    }

    @Test
    void find_ShouldReturnWorkspace_WhenFound() {
        UUID id = UUID.randomUUID();
        Workspace workspace = new Workspace(WorkspaceType.PRIVATE, BigDecimal.valueOf(500.0), true);
        when(workspaceRepository.find(id)).thenReturn(Optional.of(workspace));

        Workspace result = workspaceService.find(id);

        assertSame(workspace, result);
        verify(workspaceRepository).find(id);
    }

    @Test
    void findAll_ShouldReturnAllWorkspaces() {
        Set<Workspace> allWorkspaces = new HashSet<>();
        allWorkspaces.add(new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(500.0), true));
        allWorkspaces.add(new Workspace(WorkspaceType.ROOM, BigDecimal.valueOf(200.0), false));

        when(workspaceRepository.findAll()).thenReturn(allWorkspaces);

        Set<Workspace> result = workspaceService.findAll();

        assertSame(allWorkspaces, result);
        verify(workspaceRepository).findAll();
    }

    @Test
    void findAvailable_ShouldReturnAvailableWorkspaces() {
        Set<Workspace> available = new HashSet<>();
        available.add(new Workspace(WorkspaceType.PRIVATE, BigDecimal.valueOf(500.0), true));

        when(workspaceRepository.findAvailable()).thenReturn(available);

        Set<Workspace> result = workspaceService.findAvailable();

        assertSame(available, result);
        verify(workspaceRepository).findAvailable();
    }

    @Test
    void update_ShouldThrowException_WhenIdOrDtoIsNull() {
        WorkspaceDTO dto = new WorkspaceDTO(WorkspaceType.ROOM, BigDecimal.valueOf(1000.0), true);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> workspaceService.update(null, dto));
        assertEquals("WorkspaceDTO or ID cannot be null!", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> workspaceService.update(UUID.randomUUID(), null));
        assertEquals("WorkspaceDTO or ID cannot be null!", ex2.getMessage());

        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void update_ShouldThrowWorkspaceNotFoundException_WhenWorkspaceNotFound() {
        UUID id = UUID.randomUUID();
        WorkspaceDTO dto = new WorkspaceDTO(WorkspaceType.ROOM, BigDecimal.valueOf(1000.0), true);
        when(workspaceRepository.find(id)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.update(id, dto));
        verify(workspaceRepository).find(id);
    }

    @Test
    void update_ShouldModifyAndUpdateWorkspace() {
        UUID id = UUID.randomUUID();
        Workspace existing = new Workspace(WorkspaceType.OPEN_SPACE, BigDecimal.valueOf(500.0), false);
        WorkspaceDTO dto = new WorkspaceDTO(WorkspaceType.ROOM, BigDecimal.valueOf(1500.0), true);

        when(workspaceRepository.find(id)).thenReturn(Optional.of(existing));
        when(workspaceRepository.update(existing)).thenReturn(existing);

        Workspace updated = workspaceService.update(id, dto);

        assertAll(
                () -> assertEquals(dto.getType(), updated.getType()),
                () -> assertEquals(dto.getPrice(), updated.getPrice()),
                () -> assertEquals(dto.isAvailable(), updated.isAvailable())
        );
        verify(workspaceRepository).find(id);
        verify(workspaceRepository).update(existing);
    }

    @Test
    void remove_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> workspaceService.remove(null));
        assertEquals("Workspace ID cannot be null!", ex.getMessage());
        verifyNoInteractions(workspaceRepository);
    }

    @Test
    void remove_ShouldCallRepositoryRemove() {
        UUID id = UUID.randomUUID();
        when(workspaceRepository.remove(id)).thenReturn(true);

        boolean result = workspaceService.remove(id);

        assertTrue(result);
        verify(workspaceRepository).remove(id);
    }
}
