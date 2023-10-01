package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item createItem(ItemDto itemDto, long userId) {
        Item item = new Item(itemDto.getName(), itemDto.getDescription(), itemDto.isAvailable());
        User user = userDao.getUserById(userId);
        itemDao.saveItem(item, user);
        return null;
    }

    @Override
    public void updateItem() {

    }

    @Override
    public void getItem() {

    }

    @Override
    public void deleteItem() {

    }
}
