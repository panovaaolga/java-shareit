package ru.practicum.shareit.user.service;

import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto) throws ValidationException, EmailDuplicationException;

    User updateUser(UserDto userDto, long userId) throws UserNotFoundException;

    UserDto getUserById(long userId) throws UserNotFoundException;

    List<UserDto> getAllUsers();

    void deleteUser(long userId);

}
