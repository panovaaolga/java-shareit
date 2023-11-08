package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidationGroups;
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
    public User create(@Validated(ValidationGroups.Create.class) @RequestBody UserDto userDto) throws ValidationException, EmailDuplicationException {
       User user = userService.createUser(userDto);
        log.info("User created: {}", user);
      //  return userService.createUser(userDto);
        return user;
    }

    @PatchMapping("/{userId}")
    public User update(@Validated(ValidationGroups.Update.class) @RequestBody UserDto userDto, @PathVariable long userId) throws UserNotFoundException, ValidationException, EmailDuplicationException {
       User user = userService.updateUser(userDto, userId);
        log.info("User updated: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) throws UserNotFoundException {
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
