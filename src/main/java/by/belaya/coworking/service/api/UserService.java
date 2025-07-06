package by.belaya.coworking.service.api;

import by.belaya.coworking.dto.user.*;
import by.belaya.coworking.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserRegistrationRequestDto createDto);

    UserResponseDto update(UUID id, UserUpdateRequestDto updateDto);

    void delete(UUID id);

    UserResponseDto getById(UUID id);

    List<UserResponseDto> getAll();

    UserResponseDto login(UserLoginRequestDto loginDto);

}
