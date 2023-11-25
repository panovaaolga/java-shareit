package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.InsufficientPermissionException;
import ru.practicum.shareit.user.ValidationException;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId) throws NotFoundException;

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException, ValidationException;

    ItemDto getItem(long itemId) throws NotFoundException;

    List<ItemDto> getAllItemsByUser(long userId);

    void deleteItem(long userId, long itemId);

    List<ItemDto> getSearchedItems(String text);

    CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId) throws NotFoundException;

    ItemDto save(ItemDto itemDto, long userId) throws NotFoundException;

    ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException, InsufficientPermissionException;
}
