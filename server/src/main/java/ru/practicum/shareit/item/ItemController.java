package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.service.ItemService;

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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        ItemDto itemDtoNew = itemService.save(itemDto, userId);
        log.info("Item created: {}", itemDtoNew);
        return itemDtoNew;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDtoWithDates> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return itemService.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithDates getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long itemId) {
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
                                       @RequestBody CommentDtoInput commentDtoInput) {
        return itemService.addComment(commentDtoInput, authorId, itemId);
    }
}
