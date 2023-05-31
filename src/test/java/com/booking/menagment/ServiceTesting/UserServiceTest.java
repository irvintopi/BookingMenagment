package com.booking.menagment.ServiceTesting;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.model.enums.RoleEnum;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import com.booking.menagment.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

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
    void testFindAll() {
        User user = new User();
        user.setFirstName("xhoni");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        assertEquals(1, userService.findAll().size());
    }

    @Test
    void testFindByEmail() {
        //Not finished
        String email = "vini@example.com";
        User user = new User();
        user.setFirstName("Vini");
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userRepository.findByEmail(email).get();

        assertNotNull(result, "UserDTO should not be null");
        assertEquals(user.getFirstName(), result.getFirstName(), "First name should match");
        assertEquals(user.getEmail(), result.getEmail(), "Email should match");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testUpdate() {

        String email = "alibou@example.com";
        User existingUser = new User();
        existingUser.setFirstName("Ali");
        existingUser.setEmail(email);
        existingUser.setId(1);

        User updatedUser = new User();
        updatedUser.setFirstName("Updated Ali");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.update(email, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(existingUser.getEmail(), result.getEmail());

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testDelete() {

        String email = "example@example.com";
        User user = new User();
        user.setFirstName("John");
        user.setEmail(email);
        user.setRole(RoleEnum.TRAVELLER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.delete(email);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully.", response.getBody());

        verify(userRepository).findByEmail(email);
        verify(userRepository).delete(user);
    }

    @Test
    void testFindUsersOnFlight() {
        Integer flightId = 123;
        User user = new User();
        user.setFirstName("John");
        List<Booking> bookings = Collections.singletonList(new Booking(user));

        when(bookingService.findByFlightId(flightId)).thenReturn(bookings);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        when(userMapper.toDto(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.findUsersOnFlight(flightId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getFirstName(), result.get(0).getFirstName());

        verify(bookingService).findByFlightId(flightId);
        verify(userRepository).findById(user.getId());
        verify(userMapper).toDto(user);
    }
}


