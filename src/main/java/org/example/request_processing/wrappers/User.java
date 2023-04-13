package org.example.request_processing.wrappers;

import org.kohsuke.github.GHUser;

public class User extends GHUser {

    public User(String login) {
        this.login = login;
    }
}
