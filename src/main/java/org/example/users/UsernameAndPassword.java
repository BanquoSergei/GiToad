package org.example.users;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsernameAndPassword {

    private String username;

    private String password;

    public String get() {return password;}
}
