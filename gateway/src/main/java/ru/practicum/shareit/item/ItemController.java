package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ValidationGroups;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated(ValidationGroups.Create.class) @RequestBody ItemDto itemDto) {
        log.info("Item create with data {} by user with id {}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @Validated(ValidationGroups.Update.class) @RequestBody ItemDto itemDto) {
        log.info("Item update with id {} and data {} by user with id {}", itemId, itemDto, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get all items of user with id {}", userId);
        return itemClient.getAllItemsOfUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long itemId) {
        log.info("Get item with id {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchedItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam String text) {
        log.info("Search item with {} by user with id {}", text, userId);
        return itemClient.getSearchedItems(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Delete item with id {} by user with id {}", itemId, userId);
        itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long authorId,
                                       @PathVariable long itemId,
                                       @Validated @RequestBody CommentDtoInput commentDtoInput) {
        log.info("Add comment {} to item with id {} by user with id {}", commentDtoInput, itemId, authorId);
        return itemClient.addComment(authorId, itemId, commentDtoInput);
    }
}
