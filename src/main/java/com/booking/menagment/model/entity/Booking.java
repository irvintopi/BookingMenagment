package com.booking.menagment.model.entity;

import com.booking.menagment.model.enums.BookingStatusEnum;
import com.booking.menagment.model.enums.FlightClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@RequiredArgsConstructor
@Entity
@Table
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @Column(name = "flight_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightClass flightClass;

    @Column(name = "seats_booked")
    private Integer seatsBooked;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "booking_flight",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    private List<Flight> flights;

    public Booking(User user) {
        this.user = user;
    }
}
