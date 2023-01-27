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

    @Lob
    @Column
    private byte[] username;

    @Lob
    @Column
    private byte[] password;

    @Lob
    @Column
    private byte[] oauthToken;

    @Lob
    @Column
    private byte[] installationToken;

    @Lob
    @Column
    private byte[] jwtToken;
}
