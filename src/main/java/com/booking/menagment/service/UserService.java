package com.booking.menagment.service;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    /*User save(User u);
    User update(Integer id, User updatedUser);
    List<UserDTO> findUsersOnFlight(Integer flightId);
    void delete(Integer id);
    List<BookingDTO> findAllBookings(Integer id);
    BookingDTO findBookingByIdAndUser(Integer bookingId, Integer id);
    UserDTO findById(Integer id);*/
}
