package com.sg.bierkasse.services;

import com.sg.bierkasse.entities.UserEntity;
import com.sg.bierkasse.repositories.MongoDBUserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SGUserDetailsService implements UserDetailsService {

    private final MongoDBUserRepo userRepository;

    public SGUserDetailsService(MongoDBUserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("Username " + username + " not found.")
        );

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(permission ->
                        (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + permission.name())) // the prefix is important for spring security
                .toList();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
