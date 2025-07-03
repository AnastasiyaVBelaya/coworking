package by.belaya.coworking.model;

public class UserDTO {
    private String login;

    public UserDTO() {
    }

    public UserDTO(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
