package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(long authorId, ItemRequestDtoInput dtoInput);

    ItemRequestDto getRequestById(long userId, long requestId);

    List<ItemRequestDto> getRequestsByOwner(long userId);

    List<ItemRequestDto> getAllRequests(long userId, int from, int size);
}
