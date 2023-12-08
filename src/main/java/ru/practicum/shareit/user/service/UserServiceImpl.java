package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public User save(UserDto userDto) {

        return userRepository.save(UserMapper.mapToUser(userDto));
    }

    @Transactional
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
      if (userRepository.existsById(userId)) {
          userRepository.deleteById(userId);
      } else {
          throw new NotFoundException(User.class.getName());
      }
    }
}

