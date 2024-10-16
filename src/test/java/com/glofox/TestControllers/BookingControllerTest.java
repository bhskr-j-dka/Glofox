package com.glofox.TestControllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.glofox.controller.BookingController;
import com.glofox.model.Booking;
import com.glofox.model.ErrorResponse;
import com.glofox.service.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setId(1L); // Set an ID for the created booking
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        ResponseEntity<?> responseEntity = bookingController.createBooking(booking);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(booking, responseEntity.getBody());
        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }

    @Test
    void testCreateBooking_Failure() {
        String errorMessage = "Invalid booking data";
        when(bookingService.createBooking(any(Booking.class))).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> responseEntity = bookingController.createBooking(new Booking());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
    }

    @Test
    void testGetAllBookings_NotEmpty() {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<List<Booking>> responseEntity = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookings, responseEntity.getBody());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetAllBookings_Empty() {
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList());

        ResponseEntity<List<Booking>> responseEntity = bookingController.getAllBookings();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testUpdateBooking_Success() {
        Long bookingId = 1L;
        Booking updatedBooking = new Booking();
        updatedBooking.setId(bookingId);
        when(bookingService.updateBooking(eq(bookingId), any(Booking.class))).thenReturn(updatedBooking);

        ResponseEntity<?> responseEntity = bookingController.updateBooking(bookingId, updatedBooking);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBooking, responseEntity.getBody());
        verify(bookingService, times(1)).updateBooking(eq(bookingId), any(Booking.class));
    }

    @Test
    void testUpdateBooking_Failure() {
        Long bookingId = 1L;
        String errorMessage = "Booking not found";
        when(bookingService.updateBooking(eq(bookingId), any(Booking.class))).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> responseEntity = bookingController.updateBooking(bookingId, new Booking());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
    }
}
