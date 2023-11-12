package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemDaoImpl implements ItemDao {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final static long INCREASE_COUNT = 1;
    private static final long MIN_COUNT = 0;
    private long count = MIN_COUNT;

    @Override
    public Item saveItem(long userId, Item item) {
        item.setItemId(countId());
        items.compute(userId, (uId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        log.info("Item created: {}", item);
        return item;
    }

    @Override
    public Item updateItem(long userId, Item item) throws UserNotFoundException {
        if (items.containsKey(userId) && isOwner(userId, item.getItemId())) {
            Item updatedItem = items.get(userId).stream()
                    .filter(i -> i.getItemId().equals(item.getItemId())).findFirst().orElseThrow();
            updatedItem.setName(item.getName());
            updatedItem.setDescription(item.getDescription());
            updatedItem.setAvailable(item.getAvailable());
            log.info("Updated item: {}", getItemById(item.getItemId()));
            return updatedItem;
        } else {
            throw new UserNotFoundException("You are not owner of this item");
        }
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        if (items.containsKey(userId) && isOwner(userId, itemId)) {
            items.get(userId)
                    .removeIf(item -> item.getItemId().equals(itemId));
        }
    }

    @Override
    public Item getItemById(long itemId) {
        return items
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(id -> id.getItemId() == itemId)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        return items.getOrDefault(userId, Collections.EMPTY_LIST);
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item i : items.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList())) {
                if (i.getAvailable() && (StringUtils.containsIgnoreCase(i.getName(), text)
                        || StringUtils.containsIgnoreCase(i.getDescription(), text))) {
                    searchedItems.add(i);
                }
            }
        }
        log.info("Searched items: {}", searchedItems);
        return searchedItems;
    }

    private long countId() {
        return ++count;
    }

    private boolean isOwner(long userId, long itemId) {
        return items.get(userId).stream()
               .map(Item::getItemId)
               .collect(Collectors.toList())
               .contains(itemId);
    }


}
