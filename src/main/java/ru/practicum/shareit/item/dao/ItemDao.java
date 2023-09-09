package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public interface ItemDao {

    Item saveItem(Item item, User user);



}
