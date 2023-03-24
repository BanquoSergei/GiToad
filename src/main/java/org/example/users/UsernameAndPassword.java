package org.example.users;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsernameAndPassword {

    private byte[] username;

    private byte[] password;
}
