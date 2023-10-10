package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserDao {

    User getUserById(long userId);

    User save(User user);

    User update(User user);

    void delete(long userId);

    List<User> getAll();

}
