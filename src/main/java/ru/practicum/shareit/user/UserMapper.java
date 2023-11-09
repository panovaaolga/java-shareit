package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserMapper {

    public static UserDto mapToUserDto(User user) {

        return new UserDto(user.getName(), user.getEmail());
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static List<UserDto> mapToUserDtoList(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        if (!users.isEmpty()) {
            for (User u : users) {
                usersDto.add(mapToUserDto(u));
            }
        }
        return usersDto;
    }
}
