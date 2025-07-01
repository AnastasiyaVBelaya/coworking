package by.belaya.coworking.service.api;

import by.belaya.coworking.model.UserDTO;
import by.belaya.coworking.repository.entity.User;

public interface IUserService {
    User login(UserDTO userDTO);
}
