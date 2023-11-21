package ru.practicum.shareit.user.service;

import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto) throws ValidationException, EmailDuplicationException;

    User updateUser(UserDto userDto, long userId) throws UserNotFoundException, ValidationException, EmailDuplicationException;

    User getUserById(long userId) throws UserNotFoundException;

    List<User> getAllUsers();

    User save(UserDto userDto);

    User update(UserDto userDto, long userId) throws UserNotFoundException;

    void deleteUser(long userId);

}
