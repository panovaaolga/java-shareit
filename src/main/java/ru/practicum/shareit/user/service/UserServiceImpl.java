package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User createUser(UserDto userDto) throws ValidationException, EmailDuplicationException {
        return userDao.save(UserMapper.mapToUser(userDto));
    }

    public User updateUser(UserDto userDto, long userId) throws UserNotFoundException, ValidationException, EmailDuplicationException {
        try {
            User user = new User();
            if (userDto.getName() != null && userDto.getEmail() != null) {
                user = UserMapper.mapToUser(userDto);
                user.setId(userId);
            } else {
                user.setId(userId);
                if (userDto.getName() != null) {
                    user.setName(userDto.getName());
                    user.setEmail(userDao.getUserById(userId).getEmail());
                }
                if (userDto.getEmail() != null) {
                    user.setEmail(userDto.getEmail());
                    user.setName(userDao.getUserById(userId).getName());
                }
            }
            return userDao.update(user);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            throw new UserNotFoundException(e.getMessage());
        } catch (EmailDuplicationException e) {
            log.info(e.getMessage());
            throw new EmailDuplicationException(e.getMessage());
        }
    }

    public User getUserById(long userId) throws UserNotFoundException {
        return userDao.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public void deleteUser(long userId) {
        userDao.delete(userId);
    }
    }

