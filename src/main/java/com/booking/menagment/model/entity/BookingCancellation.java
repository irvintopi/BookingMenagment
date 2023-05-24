package com.booking.menagment.model.entity;

import com.booking.menagment.model.enums.CancellationStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "booking_cancellation")
@Data
public class BookingCancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "booking_id")
    private Integer bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CancellationStatusEnum status;

    @Column(name = "decline_reason")
    private String declineReason;

    @Column(name = "requested_by")
    private String email;

    @Column(name = "approved_by")
    private Integer adminID;
}
