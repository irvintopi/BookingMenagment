package com.booking.menagment.model.entity;

import com.booking.menagment.model.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "seats_economy")
    private Integer seatsEconomy;

    @Column(name = "seats_premium_economy")
    private Integer seatsPremiumEconomy;

    @Column(name = "seats_business")
    private Integer seatsBusiness;

    @Column(name = "seats_first_class", columnDefinition = "integer default 0")
    private Integer seatsFirstClass;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "flights")
    private List<Booking> bookings;
}
