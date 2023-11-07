package ru.practicum.shareit.user.dao;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.item.UserNotFoundException;
import ru.practicum.shareit.user.EmailDuplicationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UserDaoImpl implements UserDao {
    private Map<Long, User> usersMap = new HashMap<>();
    private final static long INCREASE_COUNT = 1;
    private final static long MIN_COUNT = 0;

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
            user.setUserId(countId());
            usersMap.put(user.getUserId(), user);
            return user;
        } else {
            throw new EmailDuplicationException("Email is not available");
        }
    }

    @Override
    public User update(User user) throws EmailDuplicationException {
        if (emailIsAvailable(user.getEmail())) {
            usersMap.replace(user.getUserId(), usersMap.get(user.getUserId()), user);
            return usersMap.get(user.getUserId());
        } else {
            throw new EmailDuplicationException("Email is not available");
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
        long lastId = usersMap.keySet()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(MIN_COUNT);
        return lastId + INCREASE_COUNT;
    }

    private boolean emailIsAvailable(String email) {
        return usersMap.values().stream().map(User::getEmail).filter(e -> StringUtils.equalsIgnoreCase(e, email))
                .findAny().isEmpty();
    }
}
