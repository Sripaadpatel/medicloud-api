package com.medicloud.module.auth;

import com.medicloud.module.auth.dto.JwtAuthResponse;
import com.medicloud.module.auth.dto.LoginRequest;
import com.medicloud.module.auth.dto.RegisterRequest;
import com.medicloud.module.user.model.Role;
import com.medicloud.module.user.model.User;
import com.medicloud.module.user.repository.UserRepository;
import com.medicloud.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    // These are injected automatically by @RequiredArgsConstructor
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthResponse login(LoginRequest loginRequest) {
        // This line tells Spring Security to try and authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // If successful, set the authentication in the context...
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ...and generate a token to send back to the client
        String token = jwtTokenProvider.generateToken(authentication);
        return new JwtAuthResponse(token);
    }

    public String register(RegisterRequest registerRequest) {
        // Check if email is already in use
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already taken!");
        }

        // Create new user's account
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        // (Fulfills SRS-SEC-01: Passwords must be hashed)
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        // New users are *always* just patients.
        user.setRoles(Set.of(Role.ROLE_PATIENT));

        userRepository.save(user);

        return "User registered successfully!";
    }
}