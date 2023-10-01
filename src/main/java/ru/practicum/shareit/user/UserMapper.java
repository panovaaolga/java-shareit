package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return null; //код
    }

    public static User mapToNewUser (UserDto userDto) {
        User user = new User();
        return null; //код
    }

    public static User mapToExistingUser(UserDto userDto, Long userId) {
        return null; //код
    }

    public static List<UserDto> mapToUserDtoList(Iterable<User> users) {
        return null; //код
    }
}
