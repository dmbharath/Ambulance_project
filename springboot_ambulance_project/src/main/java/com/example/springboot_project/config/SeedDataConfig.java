package com.example.springboot_project.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.springboot_project.models.Role;
import com.example.springboot_project.models.User;
import com.example.springboot_project.repositories.UserRepository;
import com.example.springboot_project.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        
      if (userRepository.count() == 0) {

        User admin = User
                      .builder()
                      .fullName("D M Bharath")
                      .phoneNumber("9731784368")
                      // .lastName("admin")
                      .email("bharath@gmail.com")
                      .password(passwordEncoder.encode("bharath"))
                      .role(Role.ROLE_ADMIN)
                      .build();

        userService.save(admin);
        log.debug("created ADMIN user - {}", admin);
      }
    }

}