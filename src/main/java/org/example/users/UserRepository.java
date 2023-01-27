package org.example.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, CrudRepository<User, String> {

    @Query(
            nativeQuery = true,
            value = "select username, password from accounts where id =:?id and username is not null and password is not null"
    )
    List<byte[]> findUsernameAndPasswordById(String id);

    byte[] findOauthTokenById(String id);
    byte[] findJwtById(String id);
    byte[] findInstallationTokenById(String id);
}
