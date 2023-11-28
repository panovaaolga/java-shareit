package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ValidationGroups;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.InsufficientPermissionException;
import ru.practicum.shareit.user.ValidationException;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @Validated(ValidationGroups.Create.class) @RequestBody ItemDto itemDto)
            throws NotFoundException {
        ItemDto itemDtoNew = itemService.save(itemDto, userId);
        log.info("Item created: {}", itemDtoNew);
        return itemDtoNew;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @Validated(ValidationGroups.Update.class) @RequestBody ItemDto itemDto)
            throws NotFoundException, InsufficientPermissionException {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDtoWithDates> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithDates getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long itemId) throws NotFoundException {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchedItems(@RequestParam String text) {
        return itemService.getSearchedItems(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDtoOutput addComment(@RequestHeader("X-Sharer-User-Id") long authorId,
                                       @PathVariable long itemId,
                                       @Validated @RequestBody CommentDtoInput commentDtoInput)
            throws NotFoundException, ValidationException {
        return itemService.addComment(commentDtoInput, authorId, itemId);
    }

}
