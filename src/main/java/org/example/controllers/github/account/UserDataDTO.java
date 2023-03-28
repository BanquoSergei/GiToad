package org.example.controllers.github.account;

public record UserDataDTO(String id, String login, String password, String jwt, String installation, String oauth) {

    public boolean correctUsernameAndPassword() {

        return (login == null) == (password == null);
    }

    public boolean nonNullUsernameAndPassword() {

        return login != null && password != null;
    }
}
