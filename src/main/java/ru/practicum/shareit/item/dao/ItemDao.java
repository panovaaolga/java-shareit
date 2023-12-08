package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.ValidationException;

import java.util.List;

public interface ItemDao {

    Item saveItem(long userId, Item item);

    Item updateItem(long userId, Item item) throws ValidationException;

    void deleteItem(long userId, long itemId);

    Item getItemById(long itemId);

    List<Item> getAllItemsByUser(long userId);

    List<Item> getSearchedItems(String text);



}
