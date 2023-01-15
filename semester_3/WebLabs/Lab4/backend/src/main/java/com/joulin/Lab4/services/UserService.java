package com.joulin.Lab4.services;

import com.joulin.Lab4.security.jwt.JwtUtils;
import com.joulin.Lab4.security.sevices.AuthUsersDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.joulin.Lab4.dto.AuthorizedUserCredentials;
import com.joulin.Lab4.dto.UserCredentials;
import com.joulin.Lab4.entities.UserEntity;
import com.joulin.Lab4.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UserService {
    private final int MIN_USERNAME_LENGTH = 4;
    private final int MIN_PASSWORD_LENGTH = 6;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public UserCredentials findByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (!user.isPresent())
            throw new UsernameNotFoundException("User not found by given username");

        return new UserCredentials(username, user.get().getPassword());
    }

    public AuthorizedUserCredentials login(UserCredentials userCredentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        return new AuthorizedUserCredentials(userCredentials.getUsername(), jwtToken);
    }

    @Transactional
    public AuthorizedUserCredentials signUp(UserCredentials userCredentials) {
        if (userRepository.existsByUsername(userCredentials.getUsername())) {
            throw new BadCredentialsException("This username is already taken");
        }
        if (userCredentials.getUsername().length() < MIN_USERNAME_LENGTH) {
            throw new BadCredentialsException(
                    String.format("Username is too short. Min length %s", MIN_USERNAME_LENGTH)
            );
        }

        if (userCredentials.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new BadCredentialsException(
                    String.format("Password is too short. Min length %s", MIN_PASSWORD_LENGTH)
            );
        }

        UserEntity user = new UserEntity(userCredentials.encoded(encoder));
        userRepository.save(user);

        return login(userCredentials);
    }

}
