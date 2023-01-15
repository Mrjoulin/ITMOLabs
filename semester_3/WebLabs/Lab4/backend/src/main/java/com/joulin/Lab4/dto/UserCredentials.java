package com.joulin.Lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String username;
    private String password;

    public UserCredentials encoded(PasswordEncoder encoder) {
        return new UserCredentials(username, encoder.encode(password));
    }
}
