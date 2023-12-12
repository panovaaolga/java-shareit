package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.ValidationException;
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
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
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
    User owner = new User(2L, "n", "email@gmail.com");
    User booker = new User(1L, "n", "email@mail.ru");
    Item item = new Item(itemId, "name", "text", true, owner, null);
    Booking booking = new Booking(1L,
            LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
            Status.WAITING, booker, item);
    User wrongAuthor = new User(3L, "n", "desc");
    CommentDtoInput commentDtoInput = new CommentDtoInput("comment");
    Comment comment = new Comment(1L, "comment", Instant.now(), item, booker);
    Booking pastBooking = new Booking(2L,
            LocalDateTime.of(2023, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2023, Month.MARCH, 25, 14, 12),
            Status.APPROVED, booker, item);
    ItemRequest itemRequest = new ItemRequest(1L, "request", LocalDateTime.now(), wrongAuthor);

    @Test
    void save() {
        Item expectedItem = new Item(2L, item.getName(), item.getDescription(),
                item.getAvailable(), item.getOwner(), itemRequest);
        ItemDto expectedDto = new ItemDto(2L, "name", "text", true, itemRequest.getId());
        when(userService.getUserById(owner.getId())).thenReturn(owner);
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(ItemMapper.mapToNewItem(expectedDto, owner, itemRequest))).thenReturn(expectedItem);

        ItemDto itemDto = itemService.save(expectedDto, owner.getId());

        assertEquals(itemDto.getDescription(), expectedItem.getDescription());
    }

    @Test
    void save_whenRequestNotFound_thenThrow() {
        ItemDto expectedDto = new ItemDto(2L, "name", "text", true, itemRequest.getId());
        when(requestRepository.findById(itemRequest.getId())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.save(expectedDto, owner.getId()));
    }

    @Test
    void update_whenItemNotFound_thenThrow() {
        ItemDto itemDto = new ItemDto(itemId, item.getName(), item.getDescription(), item.getAvailable(), null);
        when(itemRepository.findByOwnerIdAndId(owner.getId(), itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.update(owner.getId(), itemId, itemDto));
    }

    @Test
    void update_whenUserNotOwner_thenThrow() {
        ItemDto itemDto = new ItemDto(itemId, item.getName(), item.getDescription(), item.getAvailable(), null);
        when(itemRepository.findByOwnerIdAndId(wrongAuthor.getId(), itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(InsufficientPermissionException.class, () -> itemService
                .update(wrongAuthor.getId(), itemId, itemDto));
    }

    @Test
    void update_whenDataCorrect_thenReturn() {
        Item expectedItem = new Item(itemId, item.getName(), "desc",
                item.getAvailable(), item.getOwner(), itemRequest);
        ItemDto expectedDto = new ItemDto(itemId, "name", "desc", true, itemRequest.getId());

        when(itemRepository.findByOwnerIdAndId(owner.getId(), itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getUserById(owner.getId())).thenReturn(owner);
        when(itemRepository.save(ItemMapper.mapToItem(expectedDto, itemId, owner))).thenReturn(expectedItem);

        ItemDto updatedItem = itemService.update(owner.getId(), itemId, expectedDto);

        assertEquals(expectedItem.getDescription(), updatedItem.getDescription());
    }

    @Test
    void getItem_whenItemFound_thenReturn() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(List.of());
        when(itemRepository.findByOwnerIdAndId(owner.getId(), itemId)).thenReturn(Optional.empty());

        ItemDtoWithDates returnedItem = itemService.getItem(itemId, owner.getId());

        assertEquals(item.getId(), returnedItem.getId());
        assertEquals(item.getDescription(), returnedItem.getDescription());
    }

    @Test
    void getItem_whenItemFoundWithBooking_thenReturn() {
        LocalDateTime now = LocalDateTime.now();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(List.of());
        when(itemRepository.findByOwnerIdAndId(owner.getId(), itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findNextBooking(itemId, now)).thenReturn(List.of(booking));
        when(bookingRepository.findLastBooking(itemId, now)).thenReturn(List.of());

        ItemDtoWithDates returnedItem = itemService.getItem(itemId, owner.getId());
        System.out.println(returnedItem);

        assertEquals(item.getId(), returnedItem.getId());
        assertEquals(item.getDescription(), returnedItem.getDescription());
        assertEquals(booking.getId(), returnedItem.getNextBooking().getId());
    }

    @Test
    void getItem_whenItemNotFound_thenThrow() {
        when(itemRepository.findById(itemId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.getItem(itemId, owner.getId()));
    }

    @Test
    void getAllItemsByUser_whenParamsInvalid_thenThrow() {
        assertThrows(ValidationException.class, () -> itemService.getAllItemsByUser(owner.getId(), -1, 15));
    }

    @Test
    void getAllItemsByUser_whenEmpty_thenReturnEmpty() {
        when(itemRepository.findAllByOwnerId(owner.getId(), PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        List<ItemDtoWithDates> items = itemService.getAllItemsByUser(owner.getId(), 0, 10);

        assertEquals(0, items.size());
    }

    @Test
    void getAllItemsByUser_whenNotEmpty_thenReturn() {
        when(itemRepository.findAllByOwnerId(owner.getId(), PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(item)));

        List<ItemDtoWithDates> items = itemService.getAllItemsByUser(owner.getId(), 0, 10);

        assertEquals(1, items.size());
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
        itemService.deleteItem(owner.getId(), itemId);

        verify(itemRepository, times(1)).deleteByOwnerIdAndId(owner.getId(), itemId);
    }

    @Test
    void addComment_whenAuthorCorrect_thenReturn() {
        when(userService.getUserById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findByItemIdWithComments(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(),
                LocalDateTime.now())).thenReturn(List.of(pastBooking));
        when(commentRepository.save(CommentMapper.mapToNewComment(commentDtoInput, booker, item)))
                .thenReturn(comment);

        CommentDtoOutput commentDtoOutput = itemService.addComment(commentDtoInput, booker.getId(), itemId);

        assertEquals(commentDtoInput.getText(), commentDtoOutput.getText());
    }

    @Test
    void addComment_whenAuthorCorrectCurrent_thenReturn() {
        when(userService.getUserById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.save(CommentMapper.mapToNewComment(commentDtoInput, booker, item)))
                .thenReturn(comment);
        when(itemRepository.findByItemIdWithComments(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(booker.getId(),
                LocalDateTime.now(), LocalDateTime.now())).thenReturn(List.of(pastBooking));

        CommentDtoOutput commentDtoOutput = itemService.addComment(commentDtoInput, booker.getId(), itemId);

        assertEquals(commentDtoInput.getText(), commentDtoOutput.getText());
    }

    @Test
    void addComment_whenAuthorNotBooker_thenThrow() {
        when(userService.getUserById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.findByItemIdWithComments(itemId)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> itemService
                .addComment(commentDtoInput, wrongAuthor.getId(), itemId));
    }
}
