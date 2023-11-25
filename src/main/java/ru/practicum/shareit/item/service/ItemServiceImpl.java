package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.InsufficientPermissionException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.mapToNewItem(itemDto, user);
        itemDao.saveItem(userId, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto save(ItemDto itemDto, long userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.mapToNewItem(itemDto, user);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws ValidationException {
        Item itemBefore = ItemMapper.mapToExistingItem(itemDto, itemId);
        log.info("itemBefore: {}", itemBefore);
        if (itemBefore.getName() == null) {
            itemBefore.setName(itemDao.getItemById(itemId).getName());
        }
        if (itemBefore.getDescription() == null) {
            itemBefore.setDescription(itemDao.getItemById(itemId).getDescription());
        }
        if (itemBefore.getAvailable() == null) {
            itemBefore.setAvailable(itemDao.getItemById(itemId).getAvailable());
        }
        return ItemMapper.mapToItemDto(itemDao.updateItem(userId, itemBefore));
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
    public ItemDto getItem(long itemId) throws NotFoundException {
        try {
            return ItemMapper.mapToItemDto(itemRepository.findById(itemId).orElseThrow());
        } catch (Exception e) {
            throw new NotFoundException(Item.class.getName());
        }
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        return ItemMapper.mapToItemDtoList(itemRepository.findAllByOwnerId(userId));
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
        log.info("Searched items: {}", itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text,
                text));
        return ItemMapper.mapToItemDtoList(itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text));
    }

    @Override
    public CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId) throws NotFoundException {
        User author = userService.getUserById(authorId);
        Comment comment = CommentMapper.mapToNewComment(commentDtoInput, author);
        return CommentMapper.mapToCommentOutput(commentRepository.save(comment));
    }

    private boolean isOwner(long userId, long itemId) throws NotFoundException {
        if (itemRepository.findByOwnerIdAndId(userId, itemId).isPresent()) {
            return itemRepository.findById(itemId).get().getOwner().getId() == (userId);
        } else {
            throw new NotFoundException(Item.class.getName());
        }
    }
}
