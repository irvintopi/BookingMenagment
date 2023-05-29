package com.booking.menagment.service.impl;

import com.booking.menagment.model.enums.RoleEnum;
import com.booking.menagment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        return userRepository.findByEmail(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> {
                    log.error("Username not found: {}", username);
                    return new UsernameNotFoundException("Username not found");
                });
    }

    private UserDetails toUserDetails(com.booking.menagment.model.entity.User user) {
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(RoleEnum role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
