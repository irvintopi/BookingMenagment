package com.booking.menagment.controller;

import com.booking.menagment.model.dto.CancellationDeclineDTO;
import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.service.BookingCancellationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cancellation")
@AllArgsConstructor
public class BookingCancellationController {
    BookingCancellationService bookingCancellationService;
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCancellationRequest(@RequestBody CancellationRequestDTO requestDTO){
        bookingCancellationService.requestCancellation(requestDTO);
        return ResponseEntity.ok("Cancellation Requested.");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/approve/{cancellationId}")
    public ResponseEntity<?> approveCancellationRequest(@PathVariable Integer cancellationId){
        bookingCancellationService.approveCancellation(cancellationId);
        return ResponseEntity.ok("Cancellation approved");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/decline/cancellation")
    public ResponseEntity<?> declineCancellation(@RequestBody CancellationDeclineDTO declineDTO){
        bookingCancellationService.declineCancellation(declineDTO);
        return ResponseEntity.ok("Cancellation declined");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{status}")
    public ResponseEntity<?> getCancellationsByStatus(@PathVariable String status){
        return ResponseEntity.ok(bookingCancellationService.getRequestsByStatus(status));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{cancellationId}")
    public ResponseEntity<?> getCancellationsByStatus(@PathVariable Integer cancellationId){
        return ResponseEntity.ok(bookingCancellationService.getRequestsById(cancellationId));
    }
}
