package org.example.users;

import org.example.controllers.responses.Response;
import org.example.crypt.Cryptographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    private final Cryptographer cryptographer;

    @Autowired
    public UserService(UserRepository repository, Cryptographer cryptographer) {
        this.repository = repository;
        this.cryptographer = cryptographer;
    }

    public User find(String id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable("loginByPassword")
    public UsernameAndPassword findUsernameAndPassword(String id) {

        var result = repository.findUsernameAndPasswordById(id);
        var username = new String(cryptographer.decrypt(result.get(0)));
        var password = new String(cryptographer.decrypt(result.get(1)));

        return new UsernameAndPassword(username, password);
    }

    @Cacheable("loginByJwt")
    public String findJwtToken(String id) {

        return new String(cryptographer.decrypt(repository.findJwtById(id)));
    }

    @Cacheable("loginByInstallationToken")
    public String findInstallationToken(String id) {

        return new String(cryptographer.decrypt(repository.findInstallationTokenById(id)));
    }

    @Cacheable("loginByOauthToken")
    public String findOauthToken(String id) {

        return new String(cryptographer.decrypt(repository.findOauthTokenById(id)));
    }
    public Response updateData(String id,
                               String username,
                               String password,
                               String jwtToken,
                               String installationToken,
                               String oauthToken) {

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
                           String username,
                           String password,
                           String jwtToken,
                           String installationToken,
                           String oauthToken) {

        user.setId(id);

        if(username != null && password != null) {
            user.setUsername(cryptographer.encrypt(username.getBytes()));
            user.setPassword(cryptographer.encrypt(password.getBytes()));
        }
        if(jwtToken != null)
            user.setJwtToken(cryptographer.encrypt(jwtToken.getBytes()));
        if(installationToken != null)
            user.setUsername(cryptographer.encrypt(installationToken.getBytes()));
        if(oauthToken != null)
            user.setUsername(cryptographer.encrypt(oauthToken.getBytes()));
    }
}
