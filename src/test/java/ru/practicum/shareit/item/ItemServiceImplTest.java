package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceImplTest {
    @Mock
    UserServiceImpl userService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    RequestRepository requestRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    @Captor
    private ArgumentCaptor<Booking> userArgumentCaptor;

    long itemId = 1L;
    private User user = new User(2L, "n", "email@gmail.com");
    private User booker = new User(1L, "n", "email@mail.ru");
    private Item item = new Item(itemId, "name", "text", true, user, null);
    private Booking booking = new Booking(1L,
            LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
            Status.WAITING, booker, item);
    private User author = new User(3L, "n", "desc");
    private CommentDtoInput commentDtoInput = new CommentDtoInput("comment");
    private Comment comment = new Comment(1L, "comment", Instant.now(), item, booker);
    private Booking pastBooking = new Booking(2L,
            LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
            Status.APPROVED, booker, item);

    @Test
    void save() {

    }

    @Test
    void update() {

    }

    @Test
    void getItem_whenItemFound_thenReturn() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(List.of());
        when(itemRepository.findByOwnerIdAndId(user.getId(), itemId)).thenReturn(Optional.empty());

        ItemDtoWithDates returnedItem = itemService.getItem(itemId, user.getId());

        assertEquals(item.getId(), returnedItem.getId());
        assertEquals(item.getDescription(), returnedItem.getDescription());
    }

    @Test
    void getItem_whenItemFoundWithBooking_thenReturn() {
        LocalDateTime now = LocalDateTime.now();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(List.of());
        when(itemRepository.findByOwnerIdAndId(user.getId(), itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findNextBooking(itemId, now)).thenReturn(List.of(booking));
        when(bookingRepository.findLastBooking(itemId, now)).thenReturn(List.of());

        ItemDtoWithDates returnedItem = itemService.getItem(itemId, user.getId());
        System.out.println(returnedItem);

        assertEquals(item.getId(), returnedItem.getId());
        assertEquals(item.getDescription(), returnedItem.getDescription());
        assertEquals(booking.getId(), returnedItem.getNextBooking().getId());
    }

    @Test
    void getItem_whenItemNotFound_thenThrow() {
        when(itemRepository.findById(itemId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.getItem(itemId, user.getId()));
    }

    @Test
    void getAllItemsByUser() {

    }

    @Test
    void getSearchedItems() {
        when(itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue("text", "text"))
                .thenReturn(List.of(item));

        List<ItemDto> items = itemService.getSearchedItems("text");

        assertEquals(1, items.size());
        assertEquals(item.getDescription(), items.get(0).getDescription());


    }

    @Test
    void deleteItem() {
        itemService.deleteItem(user.getId(), itemId);

        verify(itemRepository, times(1)).deleteByOwnerIdAndId(user.getId(), itemId);
    }

    @Test
    void addComment_whenAuthorCorrect_thenReturn() {
        when(userService.getUserById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.save(CommentMapper.mapToNewComment(commentDtoInput, booker, item)))
                .thenReturn(comment);
        when(itemRepository.findByItemIdWithComments(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(),
                        LocalDateTime.now())).thenReturn(List.of(pastBooking));

        CommentDtoOutput commentDtoOutput = itemService.addComment(commentDtoInput, booker.getId(), itemId);

        assertEquals(commentDtoInput.getText(), commentDtoOutput.getText());
    }
}
