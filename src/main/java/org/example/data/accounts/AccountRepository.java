package org.example.data.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, CrudRepository<Account, String> {

    @Query("select acc.jwt " +
            "from Account acc where acc.id = :id")
    byte[] findJwtById(@Param("id") String id);


    @Modifying
    @Query("update Account acc " +
            "set acc.jwt = :jwt " +
            "where acc.id = :id")
    int updateJwtById(@Param("id") String id, @Param("jwt") byte[] jwt);
}
