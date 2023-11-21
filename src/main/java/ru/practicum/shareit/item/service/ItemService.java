package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId) throws UserNotFoundException;

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto getItem(long itemId);

    List<ItemDto> getAllItemsByUser(long userId);

    void deleteItem(long userId, long itemId);

    List<ItemDto> getSearchedItems(String text);

    CommentDtoOutput addComment(CommentDtoInput commentDtoInput, long authorId) throws UserNotFoundException;
}
