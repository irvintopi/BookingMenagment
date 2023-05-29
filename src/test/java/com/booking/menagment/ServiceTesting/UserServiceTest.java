package com.booking.menagment.ServiceTesting;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import com.booking.menagment.service.impl.UserServiceImpl;
import com.booking.menagment.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private  BookingService bookingService;
    @Mock
    private UserMapper userMapper;
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder, bookingService);
    }

    @Test
    void findAll() {
        User user = new User();
        user.setFirstName("xhoni");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        assertEquals(1, userService.findAll().size());
    }
}

