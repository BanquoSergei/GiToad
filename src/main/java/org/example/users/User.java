package org.example.users;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class User {

    @Id
    @Column
    private String id;

    @Column
    @Lob
    private byte[] username;

    @Column
    @Lob
    private byte[] password;

    @Column
    @Lob
    private byte[] oauthToken;

    private Set<Authority> authorities = Set.of(Authority.USER);
    @Column
    @Lob
    private byte[] installationToken;

    @Column
    @Lob
    private byte[] jwtToken;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
