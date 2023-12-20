package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                         @Validated @RequestBody BookingDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, bookerId);
        return bookingClient.bookItem(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long bookingId,
                              @RequestParam boolean approved) {
        log.info("Booking with id {} approved {} by user with id {}", bookingId, approved, ownerId);
        return bookingClient.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam(defaultValue = "ALL") String state,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        State bookingState = State.from(state)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(defaultValue = "ALL") String state,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "10") int size) {
        State bookingState = State.from(state)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));
        log.info("Get bookings by owner with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingsByOwner(userId, bookingState, from, size);
    }
}
