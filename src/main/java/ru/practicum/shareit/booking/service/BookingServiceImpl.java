package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public Booking createBooking(BookingDto bookingDto, long bookerId) throws UserNotFoundException {
        User user = userService.getUserById(bookerId);
        Item item = ItemMapper.mapToExistingItem(itemService.getItem(bookingDto.getItemId()), bookingDto.getItemId());
        Booking booking = bookingRepository.save(BookingMapper.mapToNewBooking(bookingDto, user, item));
        return booking;
    }

    public Booking approveBooking(long ownerId, long bookingId, boolean approved) {
        return null;
    }


}
