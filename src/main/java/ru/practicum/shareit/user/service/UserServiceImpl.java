package ru.practicum.shareit.user.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserDaoImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final static long MIN_COUNT = 0;
    private long count = MIN_COUNT;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User createUser(UserDto userDto) {
        increaseCount();
        userDao.save(UserMapper.mapToExistingUser(userDto, count));
        return userDao.getUserById(count); //код
     //   userDao.save(UserMapper.mapToNewUser(userDto)); //если у поля id будет аннотация generatedValue
    }

    public User updateUser(UserDto userDto, long userId) {
        userDao.update(UserMapper.mapToExistingUser(userDto, userId));
        return userDao.getUserById(userId); //код
    }

    public UserDto getUserById(long userId) {
        User user = userDao.getUserById(userId);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.getAll();
        return UserMapper.mapToUserDtoList(users);
    }

    public void deleteUser(long userId) {
        userDao.delete(userId);
    }

    private void increaseCount() {
        count++;
    }
    }

