package org.example.github.utils;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.Response;
import org.example.crypt.Cryptographer;
import org.example.github.auth.AuthBy;
import org.example.users.UserService;
import org.example.users.UsernameAndPassword;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

@RequiredArgsConstructor
public class AccountUtils {
    private GitHub client;

    private final UserService userService;

    private final Cryptographer cryptographer;

    GitHub loginByPassword(UsernameAndPassword usernameAndPassword) throws IOException {
        return client = new GitHubBuilder()
                .withPassword(
                        decrypt(usernameAndPassword.getUsername()),
                        decrypt(usernameAndPassword.getPassword())
                )
                .build();
    }

    GitHub loginByOauthToken(byte[] oauth) throws IOException {

        client = new GitHubBuilder()
                .withOAuthToken(decrypt(oauth))
                .build();

        return client = new GitHubBuilder()
                .withOAuthToken(decrypt(oauth))
                .build();
    }

    GitHub loginByInstallationToken(byte[] installation) throws IOException {
        return client = new GitHubBuilder()
                .withAppInstallationToken(decrypt(installation))
                .build();
    }

    GitHub loginByJwt(byte[] jwt) throws IOException {

        return client = new GitHubBuilder()
                .withJwtToken(decrypt(jwt))
                .build();

    }

    private byte[] encrypt(String value) {
        return (value == null) ? null : cryptographer.encrypt(value.getBytes());
    }
    private String decrypt(byte[] value) {
        return (value == null) ? null :  new String(cryptographer.decrypt(value));
    }

    public Response updateData(String id, String username, String password, String jwtToken, String installationToken, String oauthToken) {
        return userService.updateData(
                id,
                encrypt(username),
                encrypt(password),
                encrypt(jwtToken),
                encrypt(installationToken),
                encrypt(oauthToken)
        );
    }

    SetupData setup(String id, String by) throws IOException {

        var jwt = userService.findJwtById(id);

        switch (AuthBy.valueOf(by)) {
            case OAUTH -> client = loginByOauthToken(userService.findOauthById(id));
            case INSTALLATION -> client = loginByInstallationToken(userService.findInstallationById(id));
            case PASSWORD -> client = loginByPassword(userService.findLoginById(id));
            default -> client = loginByJwt(jwt);
        }

        return new SetupData(jwt, client);
    }
}