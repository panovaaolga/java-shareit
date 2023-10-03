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

    @Override
    public User getUserById(long userId) {
        User user = usersMap.get(userId);
        return user;
    }

    @Override
    public void save(User user) {
        usersMap.put(user.getUserId(), user);
    }

    @Override
    public void update(User user) {
        usersMap.replace(user.getUserId(), usersMap.get(user.getUserId()), user); //вариант 1
        if(usersMap.containsKey(user.getUserId())) { //вариант 2
            usersMap.put(user.getUserId(), user);
        } else {
            throw new ArrayIndexOutOfBoundsException(); //код
        }
    }

    @Override
    public void delete(long userId) {
        usersMap.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(usersMap.values());
    }
}
