package org.example.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.RegistrationResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Cacheable("login")
    @Transactional
    public UsernameAndPassword findLoginById(String id) {
//        var username = repository.findUsernameById(id);
//        var password = repository.findPasswordById(id);
//
//        return new UsernameAndPassword(username, password);
        return repository.findUsernameAndPasswordById(id);
    }

    @Cacheable("jwt")
    @Transactional
    public byte[] findJwtById(String id) {
        return repository.findJwtById(id);
    }
    @Cacheable("oauth")
    public byte[] findOauthById(String id) {
        return repository.findOauthTokenById(id);
    }

    public byte[] findInstallationById(String id) {
        return repository.findInstallationTokenById(id);
    }

    public ResponseEntity<RegistrationResponse> updateData(String id,
                                                           byte[] username,
                                                           byte[] password,
                                                           byte[] jwtToken,
                                                           byte[] installationToken,
                                                           byte[] oauthToken,
                                                           ArrayList<String> violations) {


        var user = repository.findById(id).orElseGet(User::new);

        setupUser(
                user,
                id,
                username,
                password,
                jwtToken,
                installationToken,
                oauthToken,
                violations
        );

        if(!violations.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(violations));

        repository.save(user);

        return ResponseEntity.ok().body(new RegistrationResponse(violations));
    }

    private void setupUser(User user,
                           String id,
                           byte[] username,
                           byte[] password,
                           byte[] jwtToken,
                           byte[] installationToken,
                           byte[] oauthToken,
                           ArrayList<String> violations) throws IllegalStateException{

        user.setId(id);

        if(user.getJwtToken() == null && jwtToken == null)
            violations.add("JWT cannot be empty");

        if(username != null && password != null) {
            user.setUsername(username);
            user.setPassword(password);
        }
        else if((username == null) != (password == null))
            violations.add("Login and password must be specified together");
        if(jwtToken != null)
            user.setJwtToken(jwtToken);
        if(installationToken != null)
            user.setInstallationToken(installationToken);
        if(oauthToken != null)
            user.setOauthToken(oauthToken);
    }

    public boolean existsById(String id) {

        return repository.existsById(id);
    }
}
