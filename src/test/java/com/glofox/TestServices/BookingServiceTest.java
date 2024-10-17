package com.glofox.TestServices;

import com.glofox.model.Booking;
import com.glofox.model.Class;
import com.glofox.repository.BookingRepository;
import com.glofox.repository.ClassRepository;
import com.glofox.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ClassRepository classRepository;

    private Booking booking;
    private Class bookedClass;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample Booking object
        booking = new Booking();
        booking.setId(1L);
        booking.setClassId(1L);
        booking.setDate(LocalDate.of(2024, 10, 5));
        booking.setName("John Doe");

        // Initialize a sample Class object
        bookedClass = new Class();
        bookedClass.setId(1L);
        bookedClass.setStartDate(LocalDate.of(2024, 10, 1));
        bookedClass.setEndDate(LocalDate.of(2024, 10, 10));
    }

    @Test
    void testCreateBooking_Success() {
        // Mock the behavior of class repository
        when(classRepository.findById(1L)).thenReturn(Optional.of(bookedClass));
        // Mock the behavior of booking repository
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(booking);

        assertNotNull(createdBooking);
        assertEquals(booking.getId(), createdBooking.getId());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCreateBooking_InvalidClassId() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Invalid Class ID. Please provide a valid class ID.", exception.getMessage());
    }

    @Test
    void testCreateBooking_DateOutOfRange() {
        // Set an out-of-range date
        booking.setDate(LocalDate.of(2024, 10, 15));

        when(classRepository.findById(1L)).thenReturn(Optional.of(bookedClass));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Booking date must be in the range of the class start date and end date.", exception.getMessage());
    }

    @Test
    void testUpdateBooking_Success() {
        // Mock existing booking
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(classRepository.findById(1L)).thenReturn(Optional.of(bookedClass));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking updatedBooking = new Booking();
        updatedBooking.setClassId(1L);
        updatedBooking.setDate(LocalDate.of(2024, 10, 5));
        updatedBooking.setName("Jane Doe");

        Booking result = bookingService.updateBooking(1L, updatedBooking);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testUpdateBooking_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.updateBooking(1L, booking);
        });

        assertEquals("Booking not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateBooking_InvalidClassId() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.updateBooking(1L, booking);
        });

        assertEquals("Invalid Class ID. Please provide a valid class ID.", exception.getMessage());
    }

    @Test
    void testUpdateBooking_DateOutOfRange() {
        // Set an out-of-range date
        booking.setDate(LocalDate.of(2024, 10, 15)); // Date outside the range of the class

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(classRepository.findById(1L)).thenReturn(Optional.of(bookedClass)); // Class exists

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.updateBooking(1L, booking);
        });

        assertEquals("Booking date must be in the range of the class start date and end date.", exception.getMessage());
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getAllBookings();

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("John Doe", bookings.get(0).getName());
    }

    @Test
    void testCreateBooking_EmptyName() {
        booking.setName("");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Booking name cannot be empty.", exception.getMessage());
    }

    @Test
    void testCreateBooking_NullDate() {
        booking.setDate(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Booking date cannot be null.", exception.getMessage());
    }
}
