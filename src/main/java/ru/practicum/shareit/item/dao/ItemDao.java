package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemDao {

    void saveItem(Item item);

    void updateItem(Item item);

    void deleteItem(long itemId);

    Item getItemById(long itemId);

    List<Item> getAllItemsByUser(long userId); //или лучше на вход передавать всего юзера?

    List<Item> getSearchedItems(String text);



}
