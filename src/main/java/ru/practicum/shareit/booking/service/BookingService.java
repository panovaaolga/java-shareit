package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.UnsupportedStateException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.ValidationException;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto bookingDto, long bookerId) throws NotFoundException, ValidationException;

    Booking approveBooking(long ownerId, long bookingId, boolean approved) throws NotFoundException,
            ValidationException;

    Booking getBookingById(long bookingId, long userId) throws NotFoundException;

    List<Booking> getAllByBooker(long userId, String state, int from, int size) throws UnsupportedStateException,
            NotFoundException;

    List<Booking> getAllByItemOwner(long ownerId, String state, int from, int size) throws UnsupportedStateException,
            NotFoundException;
}
