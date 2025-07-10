package by.belaya.coworking.dto.user;

import by.belaya.coworking.enums.Role;

public class UserUpdateRequestDto {

    private String login;
    private String password;
    private Role role;

    public UserUpdateRequestDto() {
    }

    public UserUpdateRequestDto(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
