package org.example.data.accounts;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column
    private String id;
    private Set<Authority> authorities = Set.of(Authority.USER);
    @Column
    private byte[] jwt;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
