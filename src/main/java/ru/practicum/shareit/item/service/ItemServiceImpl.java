package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
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
    private final static long MIN_COUNT = 0;
    private long count = MIN_COUNT;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        increaseCount();
        Item item = ItemMapper.mapToItem(itemDto, count, userDao.getUserById(userId));
        itemDao.saveItem(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        return null;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemDao.deleteItem(userId);
    }

    @Override
    public List<ItemDto> getSearchedItems(String text) {
        return ItemMapper.mapToItemDtoList(itemDao.getSearchedItems(text));
    }

    private void increaseCount() {
        count++;
    }
}
