package com.booking.menagment.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CancellationRequestDTO {
    String email;
    Integer bookingId;
}
