package ru.practicum.shareit.request;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Validated @RequestBody ItemRequestDtoInput dtoInput) {
        return null;
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return null;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam long from, @RequestParam long size) {
        return null;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return null;
    }
}
