package com.booking.menagment.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            try{
                return ResponseEntity.ok(authenticationService.register(request));
            } catch (DataIntegrityViolationException d){
                String errorMessage = "Not all required fields inserted!";
                AuthenticationResponse errorResponse = new AuthenticationResponse(errorMessage);
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = "Registration failed: " + e.getMessage();
            AuthenticationResponse errorResponse = new AuthenticationResponse(errorMessage);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

}
