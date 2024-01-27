package com.example.springboot_project.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springboot_project.dto.JwtAuthenticationResponse;
import com.example.springboot_project.dto.SignInRequest;
import com.example.springboot_project.dto.SignUpRequest;
import com.example.springboot_project.models.Role;
import com.example.springboot_project.models.User;
import com.example.springboot_project.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationResponse signup(SignUpRequest request) {
      var user = User
                  .builder()
                  .fullName(request.getFullName())
                  .phoneNumber(request.getPhoneNumber())
                  // .lastName(request.getLastName())
                  .email(request.getEmail())
                  .password(passwordEncoder.encode(request.getPassword()))
                  .role(Role.ROLE_USER)
                  .build();
      user = userService.save(user);
      var jwt = jwtService.generateToken(user);
      return JwtAuthenticationResponse.builder().token(jwt).build();
  }

  public JwtAuthenticationResponse signin(SignInRequest request) 
  {
    String username = request.getEmail();
    if(request.getEmail() == null && request.getPhoneNumber() != null)
    {
      username = request.getPhoneNumber();
    }
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, request.getPassword()));
              User user = null;
              if (request.getEmail() != null) {
                  user = userRepository.findByEmail(username)
                          .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
              } else if (request.getPhoneNumber() != null) {
                  user = userRepository.findByPhoneNumber(username)
                          .orElseThrow(() -> new IllegalArgumentException("Invalid phone number or password."));
              }      
      var jwt = jwtService.generateToken(user);
      return JwtAuthenticationResponse.builder().token(jwt).build();
  }
  
}