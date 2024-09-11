package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer count = 0;

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public User addUser(User user) {
        user.setId(calcNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь {} добавлен", user.getName());
        return user;
    }

    private Integer calcNextId() {
        return ++count;
    }
}
