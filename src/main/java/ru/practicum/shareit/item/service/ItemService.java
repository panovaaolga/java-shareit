package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDtoWithDates getItem(long itemId, long userId);

    List<ItemDtoWithDates> getAllItemsByUser(long userId, int from, int size);

    void deleteItem(long userId, long itemId);

    Item getItem(long itemId);

    List<ItemDto> getRequestedItems(long requestId);

    List<ItemDto> getSearchedItems(String text);

    CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId, long itemId);

    ItemDto save(ItemDto itemDto, long userId);

    ItemDto update(long userId, long itemId, ItemDto itemDto);
}
