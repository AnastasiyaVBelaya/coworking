package by.belaya.coworking.service;

import by.belaya.coworking.dto.reservation.ReservationCreateRequestDto;
import by.belaya.coworking.dto.reservation.ReservationResponseDto;
import by.belaya.coworking.dto.reservation.ReservationUpdateRequestDto;
import by.belaya.coworking.dto.user.UserResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.entity.Reservation;
import by.belaya.coworking.entity.User;
import by.belaya.coworking.entity.Workspace;
import by.belaya.coworking.exception.ReservationNotFoundException;
import by.belaya.coworking.repository.ReservationRepository;
import by.belaya.coworking.service.api.ReservationService;
import by.belaya.coworking.service.api.UserService;
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
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final WorkspaceService workspaceService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserService userService,
                                  WorkspaceService workspaceService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.workspaceService = workspaceService;
    }

    @CachePut(value = "reservations", key = "#result.id")
    @Caching(evict = {
            @CacheEvict(value = "reservations", key = "'all'"),
            @CacheEvict(value = "reservations", key = "#userId")
    })
    @Override
    public ReservationResponseDto create(ReservationCreateRequestDto dto, UUID userId) {
        User user = userService.getReferenceById(userId);
        Workspace workspace = workspaceService.getReferenceById(dto.getWorkspaceId());

        Reservation reservation = new Reservation(
                user,
                workspace,
                dto.getReservationDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        reservation = reservationRepository.save(reservation);

        workspace.setAvailable(false);
        workspaceService.update(workspace.getId(),
                new WorkspaceUpdateRequestDto(
                        workspace.getType(),
                        workspace.getPrice(),
                        workspace.isAvailable()
                ));

        return toDto(reservation);
    }

    @Cacheable(value = "reservations", key = "#id")
    @Override
    public ReservationResponseDto getById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return toDto(reservation);
    }

    @Cacheable(value = "reservations", key = "#userId")
    @Override
    public List<ReservationResponseDto> getByUser(UUID userId) {
        return reservationRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "reservations", key = "'all'")
    @Override
    public List<ReservationResponseDto> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "reservations", key = "#id"),
            @CacheEvict(value = "reservations", key = "'all'")
    })
    @Override
    public void delete(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservationRepository.deleteById(id);

        Workspace workspace = reservation.getWorkspace();
        workspace.setAvailable(true);
        workspaceService.update(workspace.getId(),
                new WorkspaceUpdateRequestDto(
                        workspace.getType(),
                        workspace.getPrice(),
                        workspace.isAvailable()
                ));
    }

    @CachePut(value = "reservations", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "reservations", key = "'all'"),
            @CacheEvict(value = "reservations", key = "#reservation.user.id")
    })
    @Override
    public ReservationResponseDto update(UUID id, ReservationUpdateRequestDto dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.setReservationDate(dto.getReservationDate());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());

        Reservation updated = reservationRepository.save(reservation);
        return toDto(updated);
    }

    private ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getWorkspace().getId(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt(),
                reservation.getVersion()
        );
    }

    private User mapUserDtoToEntity(UserResponseDto dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setRole(dto.getRole());
        return user;
    }

    private Workspace mapWorkspaceDtoToEntity(WorkspaceResponseDto dto) {
        Workspace workspace = new Workspace();
        workspace.setType(dto.getType());
        workspace.setPrice(dto.getPrice());
        workspace.setAvailable(dto.isAvailable());
        return workspace;
    }
}
