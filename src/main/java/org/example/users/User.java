package org.example.users;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column
    @Lob
    private byte[] installationToken;

    @Column
    @Lob
    private byte[] jwtToken;
}
