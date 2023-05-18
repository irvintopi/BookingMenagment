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

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        authenticationResponse.setToken(tokenService.generateToken(userDetailsService.loadUserByUsername(authenticationRequest.getEmail())));

        return authenticationResponse;
    }

    public AuthenticationResponse register(RegisterRequest request) {

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
