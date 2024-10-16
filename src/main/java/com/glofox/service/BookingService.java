package com.glofox.service;

import com.glofox.model.Booking;
import com.glofox.model.Class;
import com.glofox.repository.BookingRepository;
import com.glofox.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class to handle business logic related to bookings.
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ClassRepository classRepository;

    public Booking createBooking(Booking newBooking) {
        Class bookedClass = classRepository.findById(newBooking.getClassId())
                .orElseThrow(() -> new RuntimeException("Invalid Class ID. Please provide a valid class ID."));

        if (!isDateInClassRange(bookedClass, newBooking.getDate())) {
            throw new RuntimeException("Booking date must be in the range of the class start date and end date.");
        }

        return bookingRepository.save(newBooking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        Class bookedClass = classRepository.findById(updatedBooking.getClassId())
                .orElseThrow(() -> new RuntimeException("Invalid Class ID. Please provide a valid class ID."));

        if (!isDateInClassRange(bookedClass, updatedBooking.getDate())) {
            throw new RuntimeException("Booking date must be in the range of the class start date and end date.");
        }

        existingBooking.setClassId(updatedBooking.getClassId());
        existingBooking.setDate(updatedBooking.getDate());
        existingBooking.setName(updatedBooking.getName());

        return bookingRepository.save(existingBooking);
    }



    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    private boolean isDateInClassRange(Class clazz, LocalDate date) {
        return !(date.isBefore(clazz.getStartDate()) || date.isAfter(clazz.getEndDate()));
    }
}
