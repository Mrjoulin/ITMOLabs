package com.joulin.Lab4.security.sevices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joulin.Lab4.entities.UserEntity;
import com.joulin.Lab4.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthUsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthUsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (!user.isPresent())
            throw new UsernameNotFoundException("User Not Found with username: " + username);

        return AuthUsersDetails.build(user.get());
    }

}
