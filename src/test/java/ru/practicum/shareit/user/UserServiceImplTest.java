package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private long userId = 1L;
    private static final String NAME = "name";
    private static final String EMAIL = "email@mail.ru";

    @Test
    void getUserById_whenUserExists_thenReturnUser() {
        User expectedUser = new User(userId, NAME, EMAIL);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserById_whenNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers_whenEmptyList_thenReturn() {
        List<User> expectedUsers = List.of();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(0, users.size(), "List should be empty");
    }

    @Test
    void getAllUsers_whenListNotEmpty_thenReturn() {
        List<User> expectedUsers = List.of(new User(userId, NAME, EMAIL));
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getId());
    }

    @Test
    void deleteUser_whenUserFound_thenDelete() {
        when(userRepository.existsById(userId)).thenReturn(true);
        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_whenUserNotFound_thenThrow() {
        doThrow(NotFoundException.class).when(userRepository).deleteById(userId);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository, never()).deleteById(userId);
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    void updateUser_whenUserFound_thenUpdate() {
        User oldUser = new User();
        oldUser.setName("name");
        oldUser.setEmail("email@mail.ru");

        User newUser = new User();
        newUser.setName("new name");
        newUser.setEmail("email@mail.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.update(UserMapper.mapToUserDto(newUser), userId);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("new name", savedUser.getName());
        assertEquals("email@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenNotFound_thenThrow() {
        User newUser = new User();
        newUser.setName("new name");
        newUser.setEmail("email@mail.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        verify(userRepository, never()).save(newUser);
        assertThrows(NotFoundException.class, () -> userService.update(UserMapper.mapToUserDto(newUser), userId));
    }

    @Test
    void updateUser_whenEmailEmpty_thenUpdate() {
        User oldUser = new User();
        oldUser.setName(NAME);
        oldUser.setEmail(EMAIL);

        UserDto newUser = new UserDto();
        newUser.setName("new name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.update(newUser, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("new name", savedUser.getName());
        assertEquals(EMAIL, savedUser.getEmail());
    }

    @Test
    void updateUser_whenNameEmpty_thenUpdate() {
        User oldUser = new User();
        oldUser.setName(NAME);
        oldUser.setEmail(EMAIL);

        UserDto newUser = new UserDto();
        newUser.setEmail("newemail@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.update(newUser, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(NAME, savedUser.getName());
        assertEquals("newemail@gmail.com", savedUser.getEmail());
    }

    @Test
    void createUser_whenNameValid_thenReturn() {
        User expectedUser = new User(userId, NAME, EMAIL);
        UserDto userDto = new UserDto(NAME, EMAIL);
        when(userRepository.save(UserMapper.mapToUser(userDto))).thenReturn(expectedUser);

        User savedUser = userService.save(userDto);

        assertEquals(expectedUser, savedUser);
    }
}
