package org.example.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, CrudRepository<User, String> {

    byte[] findUsernameById(String id);

    byte[] findPasswordById(String id);

    @Query("select new org.example.users.UsernameAndPassword(acc.username, acc.password) " +
            "from User acc where acc.id = ?1")
    UsernameAndPassword findUsernameAndPasswordById(String id);

    byte[] findOauthTokenById(String id);

    @Query("select acc.jwtToken " +
            "from User acc where acc.id = ?1")
    byte[] findJwtById(String id);
    byte[] findInstallationTokenById(String id);
}
