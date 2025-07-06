package by.belaya.coworking.service.api;

import by.belaya.coworking.dto.reservation.ReservationCreateRequestDto;
import by.belaya.coworking.dto.reservation.ReservationResponseDto;
import by.belaya.coworking.dto.reservation.ReservationUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponseDto create(ReservationCreateRequestDto dto, UUID userId);

    ReservationResponseDto getById(UUID id);

    List<ReservationResponseDto> getByUser(UUID userId);

    List<ReservationResponseDto> getAll();

    void delete(UUID id);

    ReservationResponseDto update(UUID id, ReservationUpdateRequestDto dto);
}
