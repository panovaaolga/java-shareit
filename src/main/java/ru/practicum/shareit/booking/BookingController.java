package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.NotFoundException;
import ru.practicum.shareit.user.ValidationException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                   @Validated @RequestBody BookingDto bookingDto) throws NotFoundException, ValidationException {
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long bookingId,
                              @RequestParam boolean approved) throws NotFoundException, ValidationException {
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) throws NotFoundException {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(defaultValue = "ALL") String state)
            throws UnsupportedStateException, NotFoundException {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "ALL") String state) throws UnsupportedStateException, NotFoundException {
        return bookingService.getAllByItemOwner(userId, state);
    }
}
