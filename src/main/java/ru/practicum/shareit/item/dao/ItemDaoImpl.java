package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

public class ItemDaoImpl implements ItemDao {
   // private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> items = new HashMap<>();


    @Override
    public void saveItem(Item item) {
        items.put(item.getItemId(), item);
    }

    private void save(long userId, Item item) {

    }

    @Override
    public void updateItem(Item item) {
        try {
            items.replace(item.getItemId(), items.get(item.getItemId()), item);
        } catch (Exception e) {    //поменять исключение
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteItem(long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        return null;
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        for (Item i : items.values()) {
            if (i.isAvailable() && (i.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
                    || i.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))) {
                searchedItems.add(i);
            }
        }
        return searchedItems;
    }


}
