package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

public class ItemDaoImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private long count = 0;


    @Override
    public Item saveItem(Item item, User user) {
        increaseCount();
        item.setItemId(count);
        item.setOwner(user);
        return null;
    }

    private void increaseCount() {
        count++;
    }
}
