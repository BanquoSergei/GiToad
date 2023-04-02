package org.example.data.accounts;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    @Cacheable("jwt")
    @Transactional
    public byte[] findJwtById(String id) {
        return repository.findJwtById(id);
    }


    public void updateData(String id, byte[] jwt) {


        try {
            repository.updateJwtById(id, jwt);
        } catch (Exception e) {
            var user = new Account();
            user.setId(id);
            user.setJwt(jwt);
            repository.save(user);
        }
    }

    public boolean existsById(String id) {

        return repository.existsById(id);
    }
}
