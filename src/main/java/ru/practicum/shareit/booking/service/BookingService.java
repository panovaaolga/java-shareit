package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto bookingDto, long bookerId);

    Booking approveBooking(long ownerId, long bookingId, boolean approved);

    Booking getBookingById(long bookingId, long userId);

    List<Booking> getAllByBooker(long userId, String state, int from, int size);

    List<Booking> getAllByItemOwner(long ownerId, String state, int from, int size);
}
