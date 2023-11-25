package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDateTime;

public class BookingMapper {
    public static Booking mapToNewBooking(BookingDto bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static Booking mapToBooking(BookingDtoWithState bookingDtoWithState) {

        return null;
    }

    public static BookingDtoWithState mapToBookingDtoWithState(Booking booking) {
        BookingDtoWithState bookingDtoWithState = new BookingDtoWithState();
        bookingDtoWithState.setId(booking.getId());
        bookingDtoWithState.setStart(booking.getStart());
        bookingDtoWithState.setEnd(booking.getEnd());
        bookingDtoWithState.setStatus(booking.getStatus());
        bookingDtoWithState.setItem(booking.getItem());
        bookingDtoWithState.setBooker(booking.getBooker());
        if (booking.getStatus().equals(Status.WAITING)) {
            bookingDtoWithState.setState(State.WAITING);
        }
        if (booking.getStatus().equals(Status.REJECTED)) {
            bookingDtoWithState.setState(State.REJECTED);
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            bookingDtoWithState.setState(State.PAST);
        }
        if (booking.getStart().isAfter(LocalDateTime.now())) {
            bookingDtoWithState.setState(State.FUTURE);
        }
        if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())) {
            bookingDtoWithState.setState(State.CURRENT);
        }
        return bookingDtoWithState;
    }
}
