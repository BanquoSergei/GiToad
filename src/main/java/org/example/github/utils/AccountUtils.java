package org.example.github.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.controllers.github.account.UserDataDTO;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.RegistrationResponse;
import org.example.crypt.Cryptographer;
import org.example.github.auth.AuthBy;
import org.example.users.UserService;
import org.example.users.UsernameAndPassword;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AccountUtils {
    private GitHub client;

    private final UserService userService;

    private final Cryptographer cryptographer;

    @SneakyThrows
    GitHub loginByPassword(UsernameAndPassword usernameAndPassword) {
        return client = new GitHubBuilder()
                .withPassword(
                        decrypt(usernameAndPassword.username()),
                        decrypt(usernameAndPassword.password())
                )
                .build();
    }

    @SneakyThrows
    GitHub loginByOauthToken(byte[] oauth) {

        return client = new GitHubBuilder()
                .withOAuthToken(decrypt(oauth))
                .build();
    }

    @SneakyThrows
    GitHub loginByInstallationToken(byte[] installation) {
        return client = new GitHubBuilder()
                .withAppInstallationToken(decrypt(installation))
                .build();
    }

    @SneakyThrows
    GitHub loginByJwt(byte[] jwt) {

        return client = new GitHubBuilder()
                .withJwtToken(decrypt(jwt))
                .build();

    }

    private boolean checkByJwt(String jwt) {
        try {
            new GitHubBuilder()
                    .withJwtToken(jwt)
                    .build();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean checkByInstallation(String installation) {
        try {
            new GitHubBuilder()
                    .withAppInstallationToken(installation)
                    .build().getMyself();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    boolean checkByOauth(String oauth) {

        try {
            new GitHubBuilder()
                    .withOAuthToken(oauth)
                    .build().getMyself();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    boolean checkByLoginAndPassword(String username, String password) {
        try {
            new GitHubBuilder()
                    .withPassword(
                            username, password
                    )
                    .build().getMyself();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private byte[] encrypt(String value) {
        return (value == null) ? null : cryptographer.encrypt(value.getBytes());
    }
    private String decrypt(byte[] value) {
        return (value == null) ? null :  new String(cryptographer.decrypt(value));
    }

    public ResponseEntity<RegistrationResponse> updateData(UserDataDTO data) {

        var violations = new ArrayList<String>();

        validateAuthData(data, violations);

        if(!violations.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(violations));

        return userService.updateData(
                data.id(),
                encrypt(data.login()),
                encrypt(data.password()),
                encrypt(data.jwt()),
                encrypt(data.installation()),
                encrypt(data.oauth()),
                violations
        );
    }

    private void validateAuthData(UserDataDTO data, List<String> violations) {

        if(data.nonNullUsernameAndPassword() && !checkByLoginAndPassword(data.login(), data.password()))
            violations.add("Invalid login and password");
        if(data.jwt() != null && !checkByJwt(data.jwt()))
            violations.add("Invalid JWT");
        if(data.installation() != null && !checkByInstallation(data.installation()))
            violations.add("Invalid installation token");
        if(data.oauth() != null && !checkByOauth(data.oauth()))
            violations.add("Invalid OAUTH");
    }

    SetupData setup(String id, String by) {

        var jwt = userService.findJwtById(id);

        switch (AuthBy.valueOf(by)) {
            case OAUTH -> {

                client = loginByOauthToken(userService.findOauthById(id));
            }
            case INSTALLATION -> client = loginByInstallationToken(userService.findInstallationById(id));
            case PASSWORD -> client = loginByPassword(userService.findLoginById(id));
            default -> client = loginByJwt(jwt);
        }

        return new SetupData(jwt, client);
    }

    public ResponseEntity<LogicalStateResponse> exists(String id) {

        return ResponseEntity.ok(new LogicalStateResponse(userService.existsById(id)));
    }
}