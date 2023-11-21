package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) throws UserNotFoundException {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.mapToNewItem(itemDto, user);
        itemDao.saveItem(userId, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException {
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
    public ItemDto getItem(long itemId) {
        return ItemMapper.mapToItemDto(itemDao.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        return ItemMapper.mapToItemDtoList(itemDao.getAllItemsByUser(userId));
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemDao.deleteItem(userId, itemId);
    }

    @Override
    public List<ItemDto> getSearchedItems(String text) {
        return ItemMapper.mapToItemDtoList(itemDao.getSearchedItems(text));
    }

    @Override
    public CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId) throws UserNotFoundException {
        User author = userService.getUserById(authorId);
        Comment comment = CommentMapper.mapToNewComment(commentDtoInput, author);
        return CommentMapper.mapToCommentOutput(commentRepository.save(comment));
    }
}
