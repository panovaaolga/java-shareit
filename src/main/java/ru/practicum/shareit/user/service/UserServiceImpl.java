package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.NotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.ValidationException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserRepository userRepository;

    public User createUser(UserDto userDto) throws ValidationException, EmailDuplicationException {
        return userDao.save(UserMapper.mapToUser(userDto));
    }

    public User save(UserDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return user;
    }

    public User updateUser(UserDto userDto, long userId)
            throws NotFoundException, ValidationException, EmailDuplicationException {
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
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw new NotFoundException(User.class.getName());
        } catch (EmailDuplicationException e) {
            log.info(e.getMessage());
            throw new EmailDuplicationException(e.getMessage());
        }
    }

    
    public User update(UserDto userDto, long userId) throws NotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            if (userDto.getName() != null && userDto.getEmail() != null) {
                user.setName(userDto.getName());
                user.setEmail(userDto.getEmail());
            } else {
                if (userDto.getName() != null) {
                    user.setName(userDto.getName());
                }
                if (userDto.getEmail() != null) {
                    user.setEmail(userDto.getEmail());
                }
            }
            return userRepository.save(user);
        } else {
            throw new NotFoundException(User.class.getName());
        }
    }

    public User getUserById(long userId) throws NotFoundException {
        try {
            return userRepository.findById(userId).orElseThrow();
        } catch (Exception e) {
            throw new NotFoundException(User.class.getName());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}

