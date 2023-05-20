package com.booking.menagment.security.authentication;

import com.booking.menagment.security.config.TokenService;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.validators.MailValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final MailValidator mailValidator;
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
        if (!mailValidator.isValidEmail(request.getEmail())) {
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
}
