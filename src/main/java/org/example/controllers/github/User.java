package org.example.controllers.github;

import org.kohsuke.github.GHUser;

public class User extends GHUser {

    public User(String login) {
        this.login = login;
    }
}
