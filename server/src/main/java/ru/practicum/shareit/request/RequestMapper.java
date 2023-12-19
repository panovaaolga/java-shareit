package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class RequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDtoInput dtoInput, User author) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setAuthor(author);
        itemRequest.setDescription(dtoInput.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto mapToRequestDtoOutput(ItemRequest itemRequest, List<ItemDto> items) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }
}
