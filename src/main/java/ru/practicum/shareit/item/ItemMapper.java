package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        return itemDto;
    }

    public static List<ItemDto> mapToItemDtoList(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        if (!items.isEmpty()) {
            for (Item item : items) {
                itemDtoList.add(mapToItemDto(item));
            }
        }
        return itemDtoList;
    }

    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setOwner(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.isAvailable());
        return item;
    }

    public static Item mapToExistingItem(ItemDto itemDto) {
        return null;
    }
}
