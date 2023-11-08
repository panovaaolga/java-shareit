package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemDao {

    Item saveItem(long userId, Item item);

    Item updateItem(long userId, Item item);

    void deleteItem(long userId, long itemId);

    Item getItemById(long itemId);

    List<Item> getAllItemsByUser(long userId); //или лучше на вход передавать всего юзера?

    List<Item> getSearchedItems(String text);



}
