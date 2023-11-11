package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item saveItem(long userId, Item item);

    Item updateItem(long userId, Item item) throws UserNotFoundException;

    void deleteItem(long userId, long itemId);

    Item getItemById(long itemId);

    List<Item> getAllItemsByUser(long userId);

    List<Item> getSearchedItems(String text);



}
