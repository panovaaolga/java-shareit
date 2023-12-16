package ru.practicum.shareit.item.service;

import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.InsufficientPermissionException;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDtoWithDates getItem(long itemId, long userId) throws NotFoundException;

    List<ItemDtoWithDates> getAllItemsByUser(long userId, int from, int size);

    void deleteItem(long userId, long itemId);

    Item getItem(long itemId);

    List<ItemDto> getRequestedItems(long requestId);

    List<ItemDto> getSearchedItems(String text);

    CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId, long itemId) throws NotFoundException, ValidationException;

    ItemDto save(ItemDto itemDto, long userId) throws NotFoundException;

    ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException, InsufficientPermissionException;
}
