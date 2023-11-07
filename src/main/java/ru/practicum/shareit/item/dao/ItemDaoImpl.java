package ru.practicum.shareit.item.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ItemDaoImpl implements ItemDao {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final static long INCREASE_COUNT = 1;
    private final static long MIN_COUNT = 0;

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
        return item;
    }

    @Override
    public Item updateItem(long userId, Item item) throws UserNotFoundException {
        //добавить проверку, что юзер - владелец вещи
        if (items.get(userId).stream().map(Item::getItemId).collect(Collectors.toList()).contains(item.getItemId())) {
            items.get(userId).stream().map(Item::getItemId).filter();
        }
        throw new UserNotFoundException("You are not owner of this item");
//        try {
//            items.replace(item.getItemId(), items.get(item.getItemId()), item);
//        } catch (Exception e) {    //поменять исключение
//            throw new RuntimeException();
//        }
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        if (items.containsKey(userId)) {
            items.get(userId)
                    .removeIf(item -> item.getItemId().equals(itemId));
        }
    }

    @Override
    public Item getItemById(long itemId) {
        List<Item> allItems = items
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Item item = allItems.stream().filter(id -> id.getItemId() == itemId).findFirst().get();
        return item;
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        return items.get(userId);
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        for (Item i : items.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList())) {
//            if (i.isAvailable() && (i.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
//                    || i.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))) {
//                searchedItems.add(i);
//            }

            if (i.isAvailable() && (StringUtils.containsIgnoreCase(i.getName(), text)
                    || StringUtils.containsIgnoreCase(i.getDescription(), text))) {
                searchedItems.add(i);
            }
        }

        return searchedItems;
    }

    private long countId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getItemId)
                .max()
                .orElse(MIN_COUNT);
        return lastId + INCREASE_COUNT;
    }


}
