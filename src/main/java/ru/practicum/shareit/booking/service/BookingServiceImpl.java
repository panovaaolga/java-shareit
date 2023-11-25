package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.ErrorShareIt;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, long bookerId) throws NotFoundException, ValidationException {
        User user = userService.getUserById(bookerId);
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(Item.class.getName()));
        if (item.getOwner().getId() == bookerId) {
            throw new NotFoundException(Item.class.getName());
        }
        if (item.getAvailable()) {
            if (startDateIsBefore(bookingDto.getStart(), bookingDto.getEnd())) {
                return bookingRepository.save(BookingMapper.mapToNewBooking(bookingDto, user, item));
            } else {
                throw new ValidationException("Start date should be before end date");
            }
        } else {
            throw new ValidationException("Item is not available");
        }
    }

    @Override
    public Booking approveBooking(long ownerId, long bookingId, boolean approved) throws NotFoundException,
            ValidationException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class.getName()));
        if (booking.getItem().getOwner().getId() == ownerId) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("Impossible to change status");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            return bookingRepository.save(booking);
        }
        throw new NotFoundException(Booking.class.getName());
    }

    @Override
    public Booking getBookingById(long bookingId, long userId) throws NotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class.getName()));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return booking;
        }
        throw new NotFoundException(Booking.class.getName());
    }

    @Override
    public List<Booking> getAllByBooker(long userId, State state) throws UnsupportedStateException, NotFoundException {
        userService.getUserById(userId);
        switch(state) {
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED);
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            case FUTURE:
                log.info("Future: {}", bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now()));
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            default:
                throw new UnsupportedStateException("Unknown state: " + state.toString());
        }
    }

    public List<Booking> getAllByItemOwner(long ownerId, State state) throws UnsupportedStateException,
            NotFoundException {
        userService.getUserById(ownerId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByOwner(ownerId);
            case WAITING:
                return bookingRepository.findAllByOwnerAndStatus(ownerId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByOwnerAndStatus(ownerId, Status.REJECTED);
            case PAST:
                return bookingRepository.findAllByOwnerAndStatePast(ownerId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByOwnerAndStateCurrent(ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByOwnerAndStateFuture(ownerId, LocalDateTime.now());
            default:
                throw new UnsupportedStateException("Unknown state: " + state.toString());
        }
    }

    private boolean startDateIsBefore(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }

    @ExceptionHandler(UnsupportedStateException.class)
    private ResponseEntity handleException(UnsupportedStateException e) {
        ErrorShareIt error = new ErrorShareIt(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        return new ResponseEntity<>(error, error.getHttpStatus());
    }
}
