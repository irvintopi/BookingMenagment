package com.booking.menagment.service.impl;

import com.booking.menagment.model.enums.RoleEnum;
import com.booking.menagment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
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
