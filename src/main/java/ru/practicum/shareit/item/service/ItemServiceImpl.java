package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemDaoImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserDaoImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserServiceImpl userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) throws UserNotFoundException {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.mapToNewItem(itemDto, user);
        itemDao.saveItem(userId, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException {
//        try {
        Item item = itemDao.updateItem(userId, ItemMapper.mapToExistingItem(itemDto, itemId));
            return ItemMapper.mapToItemDto(item);
//        } catch (UserNotFoundException e) {
//            throw new UserNotFoundException("You do not have access to this item");
//        }
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
}
