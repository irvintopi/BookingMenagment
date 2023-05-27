package com.booking.menagment.repository;

import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByFlights(Flight flight);

    Booking findByUserAndFlights(User user, Flight flight);

    List<Booking> findByUser(User user);

    Page<Booking> findByUserOrderByBookingDateDesc(User user, Pageable pageable);
}
