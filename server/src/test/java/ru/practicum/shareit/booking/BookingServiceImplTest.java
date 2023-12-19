package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemService itemService;

    @Mock
    UserService userService;

    @InjectMocks
    BookingServiceImpl bookingService;

    long itemId = 1L;
    long bookerId = 1L;
    long ownerId = 2L;
    private User owner =  new User(ownerId, "n", "email@gmail.com");
    private Item item = new Item(itemId, "name", "desc", true,
            owner, null);
    private User booker = new User(bookerId, "name", "email@gmail.com");
    private BookingDto bookingDto = new BookingDto(itemId,
            LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2024, Month.MARCH, 25, 14, 12));

    @Test
    void createBooking_whenDtoCorrect_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(itemService.getItem(itemId)).thenReturn(item);
        when(bookingRepository.save(any())).thenReturn(expectedBooking);

        Booking savedBooking = bookingService.createBooking(bookingDto, bookerId);

        assertEquals(expectedBooking, savedBooking);
        assertEquals(expectedBooking.getId(), savedBooking.getId());
        assertEquals(expectedBooking.getStatus(), savedBooking.getStatus());
    }

    @Test
    void createBooking_whenDateIncorrect_thenThrow() {
        BookingDto expectedBookingDto = new BookingDto(itemId, bookingDto.getEnd(), bookingDto.getStart());
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(itemService.getItem(itemId)).thenReturn(item);

        assertThrows(ValidationException.class, () -> bookingService.createBooking(expectedBookingDto, bookerId));
    }

    @Test
    void createBooking_whenItemNotAvailable_thenThrow() {
        item.setAvailable(false);
        BookingDto expectedBookingDto = new BookingDto(itemId, bookingDto.getEnd(), bookingDto.getStart());
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(itemService.getItem(itemId)).thenReturn(item);

        assertThrows(ValidationException.class, () -> bookingService.createBooking(expectedBookingDto, bookerId));
    }

    @Test
    void createBooking_whenOwnerIsBooker_thenThrow() {
        item.setOwner(booker);
        BookingDto expectedBookingDto = new BookingDto(itemId, bookingDto.getEnd(), bookingDto.getStart());
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(itemService.getItem(itemId)).thenReturn(item);

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(expectedBookingDto, bookerId));
    }

    @Test
    void approveBooking_whenStatusWaiting_thenApprove() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        Booking approvedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.APPROVED, booker, item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(expectedBooking));
        when(bookingRepository.save(expectedBooking)).thenReturn(approvedBooking);

        Booking savedBooking = bookingService.approveBooking(2L, 1L, true);

        assertEquals(approvedBooking.getStatus(), savedBooking.getStatus());
    }

    @Test
    void approveBooking_whenStatusWaiting_thenReject() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        Booking rejectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(expectedBooking));
        when(bookingRepository.save(expectedBooking)).thenReturn(rejectedBooking);

        Booking savedBooking = bookingService.approveBooking(2L, 1L, false);

        assertEquals(rejectedBooking.getStatus(), savedBooking.getStatus());
    }

    @Test
    void approveBooking_whenBookingNotFound_thenThrow() {
        when(bookingRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> bookingService
                .approveBooking(owner.getId(), 1L, true));
    }

    @Test
    void approveBooking_whenUserNotOwner_thenThrow() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(expectedBooking));

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookerId, 1L, true));
    }

    @Test
    void approveBooking_whenStatusRejected_thenThrow() {
        Booking rejectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(rejectedBooking));
        when(bookingRepository.save(rejectedBooking)).thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, () -> bookingService
                .approveBooking(2L, 1L, true));
    }

    @Test
    void getBookingById_whenBookingFound_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

        Booking returnedBooking = bookingService.getBookingById(1L, bookerId);

        assertEquals(expectedBooking.getId(), returnedBooking.getId());
    }

    @Test
    void getBookingById_whenBookingNotFound_thenThrow() {
        when(bookingRepository.findById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, bookerId));
    }

    @Test
    void getBookingById_whenWrongUser_thenThrow() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 4L));
    }

    @Test
    void getAllByBooker_whenUserFoundAndStateWaiting_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(expectedBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING,
                PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.WAITING.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(expectedBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
    }

    @Test
    void getAllByBooker_whenUserFoundAndStateRejected_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(expectedBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED,
                PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.REJECTED.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(expectedBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
    }

    @Test
    void getAllByBooker_whenUserFoundAndStateAll_thenReturn() {
        Booking firstBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        Booking secondBooking = new Booking(2L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(firstBooking, secondBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId,
                PageRequest.of(0, 10)))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.ALL.toString(), 0, 10);

        assertEquals(2, bookings.size());
        assertEquals(firstBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(secondBooking.getStatus(), bookings.get(1).getStatus());
    }

    @Test
    void getAllByBooker_whenUserFoundAndStatePast_thenReturn() {
        Booking firstBooking = new Booking(1L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
                Status.APPROVED, booker, item);
        Booking secondBooking = new Booking(2L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(firstBooking, secondBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.PAST.toString(), 0, 10);

        assertEquals(2, bookings.size());
        assertEquals(firstBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(secondBooking.getStatus(), bookings.get(1).getStatus());
    }

    @Test
    void getAllByBooker_whenUserFoundAndStateFuture_thenReturn() {
        Booking firstBooking = new Booking(1L, LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
                Status.APPROVED, booker, item);
        Booking secondBooking = new Booking(2L, LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
                Status.REJECTED, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(firstBooking, secondBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.FUTURE.toString(), 0, 10);

        assertEquals(2, bookings.size());
        assertEquals(firstBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(secondBooking.getStatus(), bookings.get(1).getStatus());
    }

    @Test
    void getAllByBooker_whenUserFoundAndStateCurrent_thenReturn() {
        Booking currentBooking = new Booking(2L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
                Status.APPROVED, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(currentBooking));
        when(userService.getUserById(bookerId)).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(),
                any(), any())).thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByBooker(bookerId, State.CURRENT.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(currentBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(currentBooking.getStatus(), bookings.get(0).getStatus());
    }

    @Test
    void getAllByBooker_whenStatusInvalid_thenThrow() {
        when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(UnsupportedStateException.class, () -> bookingService
                .getAllByBooker(bookerId, "Unsupported state", 0, 10));
    }

    @Test
    void getAllByBooker_whenParamsInvalid_thenThrow() {
        when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(ValidationException.class, () -> bookingService
                .getAllByBooker(bookerId, State.ALL.toString(), -1, 10));
    }

    @Test
    void getAllByItemOwner_whenUserFoundAndStateWaiting_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(expectedBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwnerAndStatus(owner.getId(), Status.WAITING,
                PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(owner.getId(), State.WAITING.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(expectedBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
    }

    @Test
    void getAllByOwner_whenUserFoundAndStateAll_thenReturn() {
        Booking firstBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        Booking secondBooking = new Booking(2L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(firstBooking, secondBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwner(owner.getId(),
                PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(owner.getId(), State.ALL.toString(), 0, 10);

        assertEquals(2, bookings.size());
        assertEquals(firstBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(secondBooking.getStatus(), bookings.get(1).getStatus());
    }

    @Test
    void getAllByItemOwner_whenUserFoundAndStateRejected_thenReturn() {
        Booking expectedBooking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(),
                Status.REJECTED, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(expectedBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwnerAndStatus(owner.getId(), Status.REJECTED,
                PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(owner.getId(), State.REJECTED.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(expectedBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
    }

    @Test
    void getAllByOwner_whenUserFoundAndStatePast_thenReturn() {
        Booking firstBooking = new Booking(1L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
                Status.APPROVED, booker, item);
        Booking secondBooking = new Booking(2L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(firstBooking, secondBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwnerAndStatePast(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(owner.getId(), State.PAST.toString(), 0, 10);

        assertEquals(2, bookings.size());
        assertEquals(firstBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(secondBooking.getStatus(), bookings.get(1).getStatus());
    }

    @Test
    void getAllByOwner_whenUserFoundAndStateFuture_thenReturn() {
        Booking futureBooking = new Booking(2L, LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
                Status.WAITING, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(futureBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwnerAndStateFuture(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(ownerId, State.FUTURE.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(futureBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(futureBooking.getStatus(), bookings.get(0).getStatus());
    }

    @Test
    void getAllByOwner_whenUserFoundAndStateCurrent_thenReturn() {
        Booking currentBooking = new Booking(2L, LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
                LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
                Status.APPROVED, booker, item);
        PageImpl<Booking> bookingPage = new PageImpl<>(List.of(currentBooking));
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        when(bookingRepository.findAllByOwnerAndStateCurrent(anyLong(), any(), any()))
                .thenReturn(bookingPage);

        List<Booking> bookings = bookingService.getAllByItemOwner(ownerId, State.CURRENT.toString(), 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(currentBooking.getItem().getDescription(), bookings.get(0).getItem().getDescription());
        assertEquals(currentBooking.getStatus(), bookings.get(0).getStatus());
    }

    @Test
    void getAllByOwner_whenStatusInvalid_thenThrow() {
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        assertThrows(UnsupportedStateException.class, () -> bookingService
                .getAllByItemOwner(owner.getId(), "Unsupported state", 0, 10));
    }

    @Test
    void getAllByOwner_whenParamsInvalid_thenThrow() {
        when(userService.getUserById(owner.getId())).thenReturn(booker);
        assertThrows(ValidationException.class, () -> bookingService
                .getAllByItemOwner(owner.getId(), State.ALL.toString(), 0, 0));
    }
}
