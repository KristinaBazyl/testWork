package ru.gb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.model.Booking;
import ru.gb.model.BookingStatus;
import ru.gb.provider.UserProvider;
import ru.gb.repository.BookingRepository;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private UserProvider userProvider;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserProvider userProvider) {
        this.bookingRepository = bookingRepository;
        this.userProvider = userProvider;
    }

    public Booking createBooking(Booking booking) {
        if (userProvider.getUser(booking.getUserId()).isEmpty()) {
            throw new NoSuchElementException("Не найден пользователь с идентификатором \"" + booking.getUserId() + "\"");
        }
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreated_at(new Date());
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBooking(Long id, Booking updatedBooking){
        return bookingRepository.findById(id).map(booking -> {
            booking.setUserId(updatedBooking.getUserId());
            booking.setRoomId(updatedBooking.getRoomId());
            booking.setCheckInDate(updatedBooking.getCheckInDate());
            booking.setCheckOutDate(updatedBooking.getCheckOutDate());
            booking.setStatus(updatedBooking.getStatus());
            booking.setTotalPrice(updatedBooking.getTotalPrice());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking checkIn(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(BookingStatus.CHECKED_IN);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public Booking checkOut(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(BookingStatus.CHECKED_OUT);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }
}

