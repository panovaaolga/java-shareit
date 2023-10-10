package ru.practicum.shareit.user.dao;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UserDaoImpl implements UserDao {
    private Map<Long, User> usersMap = new HashMap<>();
    private final static long MIN_COUNT = 0;
    private long count = MIN_COUNT;

    @Override
    public User getUserById(long userId) {
        User user = usersMap.get(userId);
        return user;
    }

    @Override
    public User save(User user) {
        increaseCount();
        user.setUserId(count);
        usersMap.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        usersMap.replace(user.getUserId(), usersMap.get(user.getUserId()), user); //вариант 1
        if(usersMap.containsKey(user.getUserId())) { //вариант 2
            usersMap.put(user.getUserId(), user);
        } else {
            throw new ArrayIndexOutOfBoundsException(); //код
        }
        return usersMap.get(user.getUserId());
    }

    @Override
    public void delete(long userId) {
        usersMap.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(usersMap.values());
    }

    private void increaseCount() {
        count++;
    }
}
