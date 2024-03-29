package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking createBooking(BookingDto bookingDto, long bookerId) {
        User user = userService.getUserById(bookerId);
        Item item = itemService.getItem(bookingDto.getItemId());
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
    public Booking approveBooking(long ownerId, long bookingId, boolean approved) {
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
    public Booking getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class.getName()));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return booking;
        }
        throw new NotFoundException(Booking.class.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllByBooker(long userId, String state, int from, int size) {
        userService.getUserById(userId);
        if (from >= 0 && size > 0) {
            switch (state.toUpperCase(Locale.ROOT)) {
                case "WAITING":
                    return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING,
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "REJECTED":
                    return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED,
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "ALL":
                    return bookingRepository.findAllByBookerIdOrderByStartDesc(userId,
                            PageRequest.of(from / size, size)).getContent();
                case "PAST":
                    return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "CURRENT":
                    return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "FUTURE":
                    return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                default:
                    throw new UnsupportedStateException("Unknown state: " + state);
            }
        }
        throw new ValidationException("Params with requested values are not allowed");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllByItemOwner(long ownerId, String state, int from, int size) {
        userService.getUserById(ownerId);
        if (from >= 0 && size > 0) {
            switch (state.toUpperCase(Locale.ROOT)) {
                case "ALL":
                    return bookingRepository.findAllByOwner(ownerId,
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "WAITING":
                    return bookingRepository.findAllByOwnerAndStatus(ownerId, Status.WAITING,
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "REJECTED":
                    return bookingRepository.findAllByOwnerAndStatus(ownerId, Status.REJECTED,
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "PAST":
                    return bookingRepository.findAllByOwnerAndStatePast(ownerId, LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "CURRENT":
                    return bookingRepository.findAllByOwnerAndStateCurrent(ownerId, LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                case "FUTURE":
                    return bookingRepository.findAllByOwnerAndStateFuture(ownerId, LocalDateTime.now(),
                            PageRequest.of(from / size, size, Sort.by("start").descending())).getContent();
                default:
                    throw new UnsupportedStateException("Unknown state: " + state);
            }
        }
        throw new ValidationException("Params with requested values are not allowed");
    }

    private boolean startDateIsBefore(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }
}
