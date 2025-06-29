package service.api;

import model.UserDTO;
import repository.entity.User;

public interface IUserService {
    User login(UserDTO userDTO);
}
