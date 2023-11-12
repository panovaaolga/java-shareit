package ru.practicum.shareit.user.dao;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private Map<Long, User> usersMap = new HashMap<>();
    private long count = 0;

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        if (usersMap.containsKey(userId)) {
            return usersMap.get(userId);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public User save(User user) throws EmailDuplicationException {
        if (emailIsAvailable(user.getEmail())) {
            user.setId(countId());
            usersMap.put(user.getId(), user);
            return user;
        } else {
            log.info("Email is not available");
            throw new EmailDuplicationException("Email is not available");
        }
    }

    @Override
    public User update(User user) throws EmailDuplicationException, UserNotFoundException {
        if (usersMap.containsKey(user.getId())) {
            if (!user.getEmail().equals(usersMap.get(user.getId()).getEmail())) {
                if (emailIsAvailable(user.getEmail())) {
                    usersMap.replace(user.getId(), usersMap.get(user.getId()), user);
                } else {
                    throw new EmailDuplicationException("Email is not available");
                }
            } else {
                usersMap.put(user.getId(), user);
            }
            return usersMap.get(user.getId());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void delete(long userId) {
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(usersMap.values());
    }

    private long countId() {
        return ++count;
    }

    private boolean emailIsAvailable(String email) {
        return usersMap.values().stream().map(User::getEmail).filter(e -> StringUtils.equalsIgnoreCase(e, email))
                .findAny().isEmpty();
    }
}
