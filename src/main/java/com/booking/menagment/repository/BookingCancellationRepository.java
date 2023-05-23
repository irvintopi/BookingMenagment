package com.booking.menagment.repository;

import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.enums.CancellationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingCancellationRepository extends JpaRepository<BookingCancellation, Integer> {
    BookingCancellation findByBookingIdAndStatusIn(Integer bookingId, List<CancellationStatus> list);
}
