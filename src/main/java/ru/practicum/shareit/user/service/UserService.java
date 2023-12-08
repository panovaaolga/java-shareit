package ru.practicum.shareit.user.service;

import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserById(long userId) throws NotFoundException;

    List<User> getAllUsers();

    User save(UserDto userDto);

    User update(UserDto userDto, long userId) throws NotFoundException;

    void deleteUser(long userId);

}
