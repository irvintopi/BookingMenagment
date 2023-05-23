package com.booking.menagment.controller;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.service.BookingCancellationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cancel")
@AllArgsConstructor
public class BookingCancellationController {
    BookingCancellationService bookingCancellationService;
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCancellationRequest(@RequestBody CancellationRequestDTO requestDTO){
        bookingCancellationService.requestCancellation(requestDTO);
        return ResponseEntity.ok("Cancellation Requested.");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/approve/{cancellationId}/admin/{adminEmail}")
    public ResponseEntity<?> approveCancellationRequest(@PathVariable Integer cancellationId, @PathVariable String adminEmail){
        bookingCancellationService.approveCancellation(cancellationId, adminEmail);
        return ResponseEntity.ok("Cancellation approved");
    }
}
