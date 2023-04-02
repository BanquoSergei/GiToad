package org.example.data.github.utils;

import lombok.Getter;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.crypt.Cryptographer;
import org.example.data.accounts.AccountService;
import org.example.utils.JwtUtil;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;


public class GithubUtils {

    private final AccountService accountService;

    private final Cryptographer cryptographer;

    private final JwtUtil jwtUtil;

    private GitHub client;

    @Getter
    private AccountUtils accountUtils;

    @Getter
    private FilesUtils filesUtils;

    @Getter
    private RepositoriesUtils repositoriesUtils;

    public GithubUtils(AccountService accountService, Cryptographer cryptographer, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.cryptographer = cryptographer;
        this.jwtUtil = jwtUtil;
        accountUtils = new AccountUtils(accountService, cryptographer, jwtUtil);
    }

    public ResponseEntity<LogicalStateResponse> setup(String token) {

        var id = jwtUtil.extractId(token);

        var setupData = accountUtils.setup(id);
        client = setupData.client();
        filesUtils = new FilesUtils(client);
        repositoriesUtils = new RepositoriesUtils(setupData.jwt(), client, cryptographer);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }
}
