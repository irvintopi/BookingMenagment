package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper userMapper;


    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}
