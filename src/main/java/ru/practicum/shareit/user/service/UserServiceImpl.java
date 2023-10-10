package ru.practicum.shareit.user.service;

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
//    private final static long MIN_COUNT = 0;
//    private long count = MIN_COUNT;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getName() == null) {
            throw new RuntimeException(); //Исправить ошибку
        }
//        increaseCount();
        //  userDao.save(UserMapper.mapToUser(userDto, count)); старый вариант
        return userDao.save(UserMapper.mapToUser(userDto)); //код
    }

    public User updateUser(UserDto userDto, long userId) {
        //Здесь логика по проверке того, что пришло в DTO? Имя и email, только имя или только почта. Или в маппере?
        //Здесь проверяем наличие email у другого юзера?
        //делать ли try-catch, чтобы проверить, что юзер с таким id есть?
        User user = userDao.getUserById(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        } else if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        //userDao.update(UserMapper.mapToExistingUser(userDto, userId));
        return userDao.update(user); //код
    }

    public UserDto getUserById(long userId) {
        //надо ли добавить проверку на наличие юзера с таким id в базе или выводим пустоту?
        User user = userDao.getUserById(userId);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.getAll();
        return UserMapper.mapToUserDtoList(users);
    }

    public void deleteUser(long userId) {
        //нужна проверка на наличие такого id?
        userDao.delete(userId);
    }

//    private void increaseCount() {
//        count++;
//    }
    }

