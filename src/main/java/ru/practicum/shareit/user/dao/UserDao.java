package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.ValidationException;

import java.util.List;

public interface UserDao {

    User getUserById(long userId) throws NotFoundException;

    User save(User user) throws ValidationException, EmailDuplicationException;

    User update(User user) throws ValidationException, EmailDuplicationException, NotFoundException;

    void delete(long userId);

    List<User> getAll();

}
