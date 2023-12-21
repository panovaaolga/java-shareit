package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
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

    public static Item mapToNewItem(ItemDto itemDto, User user, ItemRequest request) {
        Item item = new Item();
        item.setOwner(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(request);
        return item;
    }

    public static Item mapToNewItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setOwner(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDtoWithDates mapToItemDtoWithDates(Item item, BookingDtoOutput lastBooking,
                                                         BookingDtoOutput nextBooking,
                                                         List<CommentDtoOutput> comments) {
        ItemDtoWithDates itemDtoWithDates = new ItemDtoWithDates();
        itemDtoWithDates.setId(item.getId());
        itemDtoWithDates.setName(item.getName());
        itemDtoWithDates.setDescription(item.getDescription());
        itemDtoWithDates.setAvailable(item.getAvailable());
        itemDtoWithDates.setLastBooking(lastBooking);
        itemDtoWithDates.setNextBooking(nextBooking);
        itemDtoWithDates.setComments(comments);
        return itemDtoWithDates;
    }
}
