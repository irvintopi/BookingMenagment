package com.booking.menagment.repository;

import com.booking.menagment.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<User, Integer> {
}
