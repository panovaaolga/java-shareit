package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);

    User updateUser(UserDto userDto, long userId);

    UserDto getUserById(long userId);

    List<UserDto> getAllUsers();

    void deleteUser(long userId);

}
