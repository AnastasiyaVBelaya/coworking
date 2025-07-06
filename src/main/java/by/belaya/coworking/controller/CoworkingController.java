package by.belaya.coworking.controller;

import by.belaya.coworking.dto.reservation.ReservationCreateRequestDto;
import by.belaya.coworking.dto.reservation.ReservationResponseDto;
import by.belaya.coworking.dto.reservation.ReservationUpdateRequestDto;
import by.belaya.coworking.dto.user.*;
import by.belaya.coworking.dto.workspace.WorkspaceCreateRequestDto;
import by.belaya.coworking.dto.workspace.WorkspaceResponseDto;
import by.belaya.coworking.dto.workspace.WorkspaceUpdateRequestDto;
import by.belaya.coworking.service.api.ReservationService;
import by.belaya.coworking.service.api.UserService;
import by.belaya.coworking.service.api.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CoworkingController {

    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public CoworkingController(UserService userService,
                               WorkspaceService workspaceService,
                               ReservationService reservationService) {
        this.userService = userService;
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRegistrationRequestDto registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(registerDto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id,
                                                      @RequestBody @Valid UserUpdateRequestDto updateDto) {
        return ResponseEntity.ok(userService.update(id, updateDto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceResponseDto>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAll());
    }

    @GetMapping("/workspaces/available")
    public ResponseEntity<List<WorkspaceResponseDto>> getAvailableWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAvailable());
    }

    @GetMapping("/workspaces/{id}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspace(@PathVariable UUID id) {
        return ResponseEntity.ok(workspaceService.getById(id));
    }

    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@RequestBody @Valid WorkspaceCreateRequestDto
                                                                            createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workspaceService.create(createDto));
    }

    @PutMapping("/workspaces/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(@PathVariable UUID id,
                                                                @RequestBody @Valid WorkspaceUpdateRequestDto
                                                                        updateDto) {
        return ResponseEntity.ok(workspaceService.update(id, updateDto));
    }

    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @GetMapping("/users/{userId}/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getUserReservations(@PathVariable UUID userId) {
        return ResponseEntity.ok(reservationService.getByUser(userId));
    }

    @PostMapping("/users/{userId}/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(@PathVariable UUID userId,
                                                                    @RequestBody @Valid ReservationCreateRequestDto
                                                                            createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(createDto, userId));
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable UUID id,
                                                                    @RequestBody @Valid ReservationUpdateRequestDto
                                                                            updateDto) {
        return ResponseEntity.ok(reservationService.update(id, updateDto));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}