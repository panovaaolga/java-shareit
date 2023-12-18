package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public ItemRequestDto createRequest(long authorId, ItemRequestDtoInput dtoInput) {
        User author = userService.getUserById(authorId);
        ItemRequest itemRequest = requestRepository.save(RequestMapper.mapToItemRequest(dtoInput, author));
        return RequestMapper.mapToRequestDtoOutput(itemRequest, new ArrayList<>());
    }

    @Override
    public ItemRequestDto getRequestById(long userId, long requestId) {
        if (userService.getUserById(userId) != null) {
            ItemRequest itemRequest = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException(ItemRequest.class.getName()));
            List<ItemDto> itemDtoList = itemService.getRequestedItems(requestId);
            return RequestMapper.mapToRequestDtoOutput(itemRequest, itemDtoList);
        }
        throw new NotFoundException(User.class.getName());
    }

    @Override
    public List<ItemRequestDto> getRequestsByOwner(long userId) {
        List<ItemRequestDto> requestDtos = new ArrayList<>();
        if (userService.getUserById(userId) != null) {
            List<ItemRequest> requests = requestRepository.findAllByAuthorIdOrderByCreatedDesc(userId);
            if (requests.isEmpty()) {
                return requestDtos;
            }
            for (ItemRequest r : requests) {
                requestDtos.add(RequestMapper.mapToRequestDtoOutput(r, itemService.getRequestedItems(r.getId())));
            }
            return requestDtos;
        }
        throw new NotFoundException(User.class.getName());
    }

    public List<ItemRequestDto> getAllRequests(long userId, int from, int size) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        if (userService.getUserById(userId) != null) {
            if (from >= 0 && size > 0) {
                Page<ItemRequest> requestPage = requestRepository.findAllByAuthorIdNot(userId, PageRequest
                        .of(from / size, size, Sort.by("created").descending()));

                if (requestPage.isEmpty()) {
                    return new ArrayList<>();
                }
                for (ItemRequest r : requestPage.getContent()) {
                    itemRequestDtos.add(RequestMapper.mapToRequestDtoOutput(r, itemService.getRequestedItems(r.getId())));
                }
                return itemRequestDtos;
            }
            throw new ValidationException("Params with requested values are not allowed");
        }
        throw new NotFoundException(User.class.getName());
    }

}
