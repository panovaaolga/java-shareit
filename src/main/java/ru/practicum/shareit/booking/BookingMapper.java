package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


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

    public static BookingDtoOutput mapToBookingDtoOutput(Booking booking) {
        BookingDtoOutput bookingDtoOutput = new BookingDtoOutput();
        bookingDtoOutput.setId(booking.getId());
        bookingDtoOutput.setBookerId(booking.getBooker().getId());
        return bookingDtoOutput;
    }
}
