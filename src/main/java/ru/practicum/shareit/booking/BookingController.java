package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

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
                                   @Validated @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long bookingId,
                              @RequestParam boolean approved) {
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam(defaultValue = "ALL") String state,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(defaultValue = "ALL") String state,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllByItemOwner(userId, state, from, size);
    }
}
