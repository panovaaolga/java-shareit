package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    Item createItem(ItemDto itemDto, long userId);

    void updateItem();

    void getItem();

    void deleteItem();
}
