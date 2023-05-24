package com.booking.menagment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationDeclineDTO {
    private Integer cancellationId;
    private String reason;
}
