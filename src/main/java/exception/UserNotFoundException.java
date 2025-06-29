package exception;

public class UserNotFoundException extends RuntimeException {
    private final String login;

    public UserNotFoundException(String login) {
        super("User with login " + login + " not found");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
