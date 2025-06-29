package model;

public class UserDTO {
    private final String login;

    public UserDTO(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
