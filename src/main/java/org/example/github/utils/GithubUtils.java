package org.example.github.utils;

import lombok.Getter;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.crypt.Cryptographer;
import org.example.users.UserService;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

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
        accountUtils = new AccountUtils(userService, cryptographer);
    }

    public ResponseEntity<LogicalStateResponse> setup(String id, String by) {

        var setupData = accountUtils.setup(id, by);
        client = setupData.client();
        filesUtils = new FilesUtils(client);
        repositoriesUtils = new RepositoriesUtils(setupData.jwt(), client, cryptographer);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }
}
