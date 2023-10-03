package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return null;
    }

    public static List<ItemDto> mapToItemDtoList(List<Item> items) {
        return null;
    }

    public static Item mapToItem(ItemDto itemDto, long itemId, User user) {
        Item item = new Item();
        item.setItemId(itemId);
        item.setOwner(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.isAvailable());
        return item;
    }
}
