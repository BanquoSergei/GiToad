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
    public UsernameAndPassword findLoginById(String id) {
        var username = repository.findUsernameById(id);
        var password = repository.findPasswordById(id);

        return new UsernameAndPassword(username, password);
    }

    @Cacheable("jwt")
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

    public boolean existsById(String id) {

        return repository.existsById(id);
    }
}
