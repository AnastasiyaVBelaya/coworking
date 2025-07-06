import by.belaya.coworking.dto.reservation.ReservationCreateRequestDto;
import by.belaya.coworking.dto.reservation.ReservationResponseDto;
import by.belaya.coworking.dto.reservation.ReservationUpdateRequestDto;
import by.belaya.coworking.dto.user.UserResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.entity.Reservation;
import by.belaya.coworking.entity.User;
import by.belaya.coworking.entity.Workspace;
import by.belaya.coworking.enums.Role;
import by.belaya.coworking.enums.WorkspaceType;
import by.belaya.coworking.exception.ReservationNotFoundException;
import by.belaya.coworking.repository.ReservationRepository;
import by.belaya.coworking.service.ReservationServiceImpl;
import by.belaya.coworking.service.api.UserService;
import by.belaya.coworking.service.api.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserService userService;

    @Mock
    private WorkspaceService workspaceService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;
    private UUID reservationId;
    private UUID userId;
    private UUID workspaceId;
    private UserResponseDto userDto;
    private WorkspaceResponseDto workspaceDto;

    @BeforeEach
    void setUp() {
        reservationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        workspaceId = UUID.randomUUID();

        userDto = new UserResponseDto(userId, "login", Role.CUSTOMER, null, null);

        workspaceDto = new WorkspaceResponseDto(workspaceId, WorkspaceType.PRIVATE, BigDecimal.TEN,
                true, null, null, 0L);

        User user = new User("login", "pass", Role.CUSTOMER);
        Workspace workspace = new Workspace(WorkspaceType.PRIVATE, BigDecimal.TEN, true);

        reservation = new Reservation(
                user,
                workspace,
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)
        );
        reservation.setWorkspace(workspace);
        reservation.setUser(user);
    }

    private void setId(UUID id, Reservation reservation) {
        try {
            Field field = Reservation.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(reservation, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create_ShouldCreateNewReservation() {
        ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto();
        requestDto.setWorkspaceId(workspaceId);
        requestDto.setReservationDate(reservation.getReservationDate());
        requestDto.setStartTime(reservation.getStartTime());
        requestDto.setEndTime(reservation.getEndTime());

        when(userService.getById(userId)).thenReturn(userDto);
        when(workspaceService.getById(workspaceId)).thenReturn(workspaceDto);
        when(workspaceService.update(isNull(), any(WorkspaceUpdateRequestDto.class))).thenReturn(workspaceDto);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation res = invocation.getArgument(0);
            setId(reservationId, res);
            return res;
        });

        ReservationResponseDto result = reservationService.create(requestDto, userDto.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userDto.getId(), result.getUserId());
        assertEquals(workspaceDto.getId(), result.getWorkspaceId());
        assertEquals(reservation.getReservationDate(), result.getReservationDate());
    }

    @Test
    void getById_ShouldReturnReservation_WhenExists() {
        setId(reservationId, reservation);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        ReservationResponseDto result = reservationService.getById(reservationId);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getById(reservationId));
    }

    @Test
    void getByUser_ShouldReturnUserReservations() {
        when(reservationRepository.findByUser_Id(userId)).thenReturn(Collections.singleton(reservation));

        List<ReservationResponseDto> result = reservationService.getByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAll_ShouldReturnAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<ReservationResponseDto> result = reservationService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void delete_ShouldDeleteReservation() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(workspaceService.update(nullable(UUID.class), any(WorkspaceUpdateRequestDto.class))).thenReturn(workspaceDto);

        reservationService.delete(reservationId);

        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.delete(reservationId));
    }

    @Test
    void update_ShouldUpdateExistingReservation() {
        ReservationUpdateRequestDto dto = new ReservationUpdateRequestDto();
        dto.setReservationDate(LocalDate.now().plusDays(2));
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(13, 0));

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationResponseDto result = reservationService.update(reservationId, dto);

        assertNotNull(result);
        assertEquals(dto.getReservationDate(), result.getReservationDate());
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        ReservationUpdateRequestDto dto = new ReservationUpdateRequestDto();
        dto.setReservationDate(LocalDate.now());
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(12, 0));

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.update(reservationId, dto));
    }
}
