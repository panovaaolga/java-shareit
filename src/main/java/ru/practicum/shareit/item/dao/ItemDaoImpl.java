package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDaoImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();


    @Override
    public void saveItem(Item item) {
        items.put(item.getItemId(), item);
    }

    @Override
    public void updateItem(Item item) {

    }

    @Override
    public void deleteItem(long itemId) {

    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        return null;
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        return null;
    }


}
