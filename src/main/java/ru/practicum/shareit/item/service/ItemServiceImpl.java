package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.item.InsufficientPermissionException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto save(ItemDto itemDto, long userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        Item item;
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException(ItemRequest.class.getName()));
            item = ItemMapper.mapToNewItem(itemDto, user, itemRequest);
            return ItemMapper.mapToItemDto(itemRepository.save(item));
        }
        item = ItemMapper.mapToNewItem(itemDto, user);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException,
            InsufficientPermissionException {
        if (isOwner(userId, itemId)) {
            Item itemBefore = itemRepository.findByOwnerIdAndId(userId, itemId).get();
            itemBefore.setOwner(userService.getUserById(userId));
            if (itemDto.getName() != null) {
                itemBefore.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemBefore.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemBefore.setAvailable(itemDto.getAvailable());
            }
            return ItemMapper.mapToItemDto(itemRepository.save(itemBefore));
        } else {
            throw new InsufficientPermissionException();
        }
    }

    @Override
    public ItemDtoWithDates getItem(long itemId, long userId) throws NotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class.getName()));
        BookingDtoOutput lastBooking = null;
        BookingDtoOutput nextBooking = null;
        List<CommentDtoOutput> comments = CommentMapper.mapToListDto(commentRepository.findByItemId(itemId));
        if (itemRepository.findByOwnerIdAndId(userId, itemId).isPresent()) {
            if (!bookingRepository.findLastBooking(itemId, LocalDateTime.now()).isEmpty()) {
                lastBooking = BookingMapper.mapToBookingDtoOutput(bookingRepository
                        .findLastBooking(itemId, LocalDateTime.now()).get(0));
            }
            if (!bookingRepository.findNextBooking(itemId, LocalDateTime.now()).isEmpty()) {
                nextBooking = BookingMapper.mapToBookingDtoOutput(bookingRepository
                        .findNextBooking(itemId, LocalDateTime.now()).get(0));
            }
        }
        return ItemMapper.mapToItemDtoWithDates(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDtoWithDates> getAllItemsByUser(long userId, int from, int size) {
        List<ItemDtoWithDates> itemsDto = new ArrayList<>();
        if (from < 0 || size <= 0) {
            throw new ValidationException("Params with requested values are not allowed");
        }
        Page<Item> items = itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size));
        BookingDtoOutput lastBooking = null;
        BookingDtoOutput nextBooking = null;
        if (items.isEmpty()) {
            return new ArrayList<>();
        }
        for (Item i : items.getContent()) {
            List<CommentDtoOutput> comments = CommentMapper.mapToListDto(commentRepository.findByItemId(i.getId()));
            if (!bookingRepository.findLastBooking(i.getId(), LocalDateTime.now()).isEmpty()) {
                lastBooking = BookingMapper.mapToBookingDtoOutput(bookingRepository
                        .findLastBooking(i.getId(), LocalDateTime.now()).get(0));
            }
            if (!bookingRepository.findNextBooking(i.getId(), LocalDateTime.now()).isEmpty()) {
                nextBooking = BookingMapper.mapToBookingDtoOutput(bookingRepository
                        .findNextBooking(i.getId(), LocalDateTime.now()).get(0));
            }
            itemsDto.add(ItemMapper.mapToItemDtoWithDates(i, lastBooking, nextBooking, comments));
            lastBooking = null;
            nextBooking = null;
        }
        return itemsDto;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    @Override
    public List<ItemDto> getSearchedItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return ItemMapper.mapToItemDtoList(itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text));
    }

    @Override
    public CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId, long itemId)
            throws NotFoundException, ValidationException {
        User author = userService.getUserById(authorId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class.getName()));
        if (isPastBooker(authorId, itemId)) {
            return CommentMapper.mapToCommentOutput(commentRepository
                    .save(CommentMapper.mapToNewComment(commentDtoInput, author, item)));
        } else {
            throw new ValidationException("You did not book this item");
        }
    }

    private boolean isOwner(long userId, long itemId) throws NotFoundException {
        if (itemRepository.findByOwnerIdAndId(userId, itemId).isPresent()) {
            return itemRepository.findById(itemId).get().getOwner().getId() == (userId);
        } else {
            throw new NotFoundException(Item.class.getName());
        }
    }

    private boolean isPastBooker(long authorId, long itemId) throws ValidationException {
        if (itemRepository.findByItemIdWithComments(itemId).isEmpty()) {
            throw new ValidationException("This item does not have this booking");
        }
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(authorId,
                LocalDateTime.now());
        for (Booking b : bookings) {
            if (b.getBooker().getId() == authorId) {
                return true;
            }
        }
        bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(authorId, LocalDateTime.now(),
                LocalDateTime.now());
        for (Booking b : bookings) {
            if (b.getBooker().getId() == authorId) {
                return true;
            }
        }
        return false;
    }
}
