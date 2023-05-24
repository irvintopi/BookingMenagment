package com.booking.menagment.repository;

import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.enums.CancellationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingCancellationRepository extends JpaRepository<BookingCancellation, Integer> {
    Optional<BookingCancellation> findByBookingIdAndStatusIn(Integer bookingId, List<CancellationStatusEnum> list);
    List<BookingCancellation> findByStatus(CancellationStatusEnum cancellationStatus);
}
