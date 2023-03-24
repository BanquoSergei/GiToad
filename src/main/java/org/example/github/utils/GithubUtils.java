package org.example.github.utils;

import lombok.Getter;
import org.example.crypt.Cryptographer;
import org.example.users.UserService;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.security.InvalidParameterException;


public class GithubUtils {

    private final UserService userService;

    private final Cryptographer cryptographer;

    private GitHub client;

    @Getter
    private AccountUtils accountUtils;

    @Getter
    private FilesUtils filesUtils;

    @Getter
    private RepositoriesUtils repositoriesUtils;

    public GithubUtils(UserService userService, Cryptographer cryptographer) {
        this.userService = userService;
        this.cryptographer = cryptographer;
    }

    public void setup(String id, String by) throws IOException {

        if(id == null)
            throw new InvalidParameterException("Parameter 'id' has been required");

        accountUtils = new AccountUtils(userService, cryptographer);
        var setupData = accountUtils.setup(id, by);
        client = setupData.client();
        filesUtils = new FilesUtils(client);
        repositoriesUtils = new RepositoriesUtils(setupData.jwt(), client, cryptographer);
    }
}
