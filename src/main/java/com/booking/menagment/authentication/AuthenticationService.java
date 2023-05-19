package com.booking.menagment.authentication;

import com.booking.menagment.model.entity.User;
import com.booking.menagment.config.TokenService;
import com.booking.menagment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        var user =repository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();
        var jwtToken = tokenService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse register(RegisterRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is invalid or already in use!");
        }
        User user1 = new User();
        BeanUtils.copyProperties(request, user1);
        user1.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user1);


        var token =  tokenService.generateToken(user1);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValidFormat = email.matches(emailRegex);

        if (!isValidFormat) {
            return false;
        }

        Optional<User> existingUser = repository.findByEmail(email);
        return existingUser.isEmpty();
    }
}
