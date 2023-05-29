package com.booking.menagment.security.authentication;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.security.config.TokenService;
import com.booking.menagment.validators.UserValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserValidator userValidator;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            log.info("Starting authentication process for email: {}", authenticationRequest.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            var user = repository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> {
                        log.error("User not found with email: {}", authenticationRequest.getEmail());
                        return new NotFoundException("User not found");
                    });

            log.info("Authentication successful for email: {}", authenticationRequest.getEmail());

            var jwtToken = tokenService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException ex) {
            log.error("Authentication failed for email: {}", authenticationRequest.getEmail(), ex);
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }


    public AuthenticationResponse register(RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);

        try {
            log.info("Starting user registration for email: {}", request.getEmail());

            userValidator.validateUser(userDTO);

            User user = new User();
            BeanUtils.copyProperties(request, user);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            repository.save(user);

            log.info("User registration successful for email: {}", request.getEmail());

            var token =  tokenService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (ValidationException ex) {
            log.error("User registration validation failed for email: {}", request.getEmail(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("User registration failed for email: {}", request.getEmail(), ex);
            throw ex;
        }
    }
}
