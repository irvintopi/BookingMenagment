package com.booking.menagment.model.entity;

import com.booking.menagment.model.enums.FlightStatus;
import jakarta.persistence.*;
import jdk.jfr.ContentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;
    @Column(nullable = false)
    private String airline;
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;
    @Column(nullable = false)
    private String origin;
    @Column(nullable = false)
    private String destination;
    @Column(name = "flight_date", nullable = false)
    private Date flightDate;
    @Column(name = "departure_time", nullable = false)
    private Time departureTime;
    @Column(name = "aircraft_type")
    private String aircraftType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus status;
    @Column(name = "economy_seats_available")
    private Integer economySeatsAvailable;
    @Column(name = "premium_economy_seats_available")
    private Integer premiumEconomySeatsAvailable;
    @Column(name = "business_seats_available")
    private Integer businessSeatsAvailable;
    @Column(name = "first_class_seats_available")
    private Integer firstClassSeatsAvailable;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "flights")
    private List<Booking> bookings;
}
