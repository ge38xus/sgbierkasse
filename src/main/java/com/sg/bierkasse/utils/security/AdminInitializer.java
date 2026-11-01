package com.sg.bierkasse.utils.security;

import com.sg.bierkasse.entities.UserEntity;
import com.sg.bierkasse.repositories.MongoDBUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final MongoDBUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {

        log.info("Checking if initial admin exists");
        if (userRepo.findByUsername("admin").isPresent()) {
            log.info("Admin lookup completed");
            return;
        }

        log.info("Creating initial admin");
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(Permissions.BIERKASSENWART));

        userRepo.save(admin);
    }
}