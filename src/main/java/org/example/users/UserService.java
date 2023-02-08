package org.example.users;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.Response;
import org.example.crypt.Cryptographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Cacheable("login")
    public User find(String id) {
        return repository.findById(id).orElseThrow();
    }

    public Response updateData(String id,
                               byte[] username,
                               byte[] password,
                               byte[] jwtToken,
                               byte[] installationToken,
                               byte[] oauthToken) {

        var user = repository.findById(id).orElseGet(User::new);

        setupUser(
                user,
                id,
                username,
                password,
                jwtToken,
                installationToken,
                oauthToken
        );
        repository.save(user);

        return Response.success();
    }

    private void setupUser(User user,
                           String id,
                           byte[] username,
                           byte[] password,
                           byte[] jwtToken,
                           byte[] installationToken,
                           byte[] oauthToken) throws IllegalStateException{

        user.setId(id);

        if(user.getJwtToken() == null && jwtToken == null)
            throw new IllegalStateException("JWT cannot be empty");

        if(username != null && password != null) {
            user.setUsername(username);
            user.setPassword(password);
        }
        if(jwtToken != null)
            user.setJwtToken(jwtToken);
        if(installationToken != null)
            user.setInstallationToken(installationToken);
        if(oauthToken != null)
            user.setOauthToken(oauthToken);
    }
}
