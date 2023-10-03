package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {

        return new UserDto(user.getName(), user.getEmail());
    }

    public static User mapToUser(UserDto userDto, Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static List<UserDto> mapToUserDtoList(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(mapToUserDto(u));
        }
        return usersDto;
    }
}
