package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

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
    public User create(@RequestBody UserDto userDto) {
        User user = userService.save(userDto);
        log.info("User created: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody UserDto userDto,
                       @PathVariable long userId) {
        User user = userService.update(userDto, userId);
        log.info("User updated: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) throws NotFoundException {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("User successfully deleted");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
