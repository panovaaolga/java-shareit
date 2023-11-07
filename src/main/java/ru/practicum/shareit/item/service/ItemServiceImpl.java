package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) throws UserNotFoundException {
        Item item = ItemMapper.mapToItem(itemDto, userDao.getUserById(userId));
        itemDao.saveItem(userId, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException {
        try {

            return null;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("You do not have access to this item");
        }
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
