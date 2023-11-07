package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) throws ValidationException {
        return userService.createUser(userDto);
    }

    @PatchMapping
    public User update(@Valid @RequestBody UserDto userDto, @PathVariable long userId) throws UserNotFoundException {
        return userService.updateUser(userDto, userId);
    }

    @GetMapping
    public UserDto getUserById(@PathVariable long userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    @DeleteMapping
    public void delete(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

}
