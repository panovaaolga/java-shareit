package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserDaoImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User createUser(UserDto userDto) throws ValidationException, EmailDuplicationException {
        if (userDto.getEmail() == null || userDto.getName() == null) {
            throw new ValidationException("Email or name should not be empty"); //Проверить ошибку
        }
        return userDao.save(UserMapper.mapToUser(userDto));
    }

    public User updateUser(UserDto userDto, long userId) throws UserNotFoundException {
        try {
            User user = userDao.getUserById(userId);
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            } else if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            return userDao.update(user); //код
        } catch (UserNotFoundException | ValidationException | EmailDuplicationException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public UserDto getUserById(long userId) throws UserNotFoundException {
        return UserMapper.mapToUserDto(userDao.getUserById(userId));
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.mapToUserDtoList(userDao.getAll());
    }

    public void deleteUser(long userId) {
        userDao.delete(userId);
    }
    }

