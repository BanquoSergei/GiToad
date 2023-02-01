package org.example.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, CrudRepository<User, String> {

    byte[] findUsernameById(String id);

    byte[] findPasswordById(String id);

    byte[] findOauthTokenById(String id);
    byte[] findJwtById(String id);
    byte[] findInstallationTokenById(String id);
}
