package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        validateUser(user);
        validateEmailDuplicates(user);
        user.setId(calcNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь {} добавлен", user.getName());
        return user;
    }

    public User updateUser(User user) {
        validateUser(user);
        Integer id = user.getId();
        if (id == null) {
            log.error("Ошибка валидации: не задан id");
            throw new ValidationException("id должен быть задан");
        }
        User oldUser = getUserById(id);
        log.info("Начало обновления информации о пользователе {}", oldUser.getName());
        if (!oldUser.getEmail().equals(user.getEmail())) {
            log.debug("Обновление email");
            oldUser.setEmail(user.getEmail());
        }
        if (!oldUser.getLogin().equals(user.getLogin())) {
            log.debug("Обновление login");
            oldUser.setLogin(user.getLogin());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            log.debug("Обновление name");
            oldUser.setName(user.getName());
        }
        if (user.getBirthday() != null) {
            log.debug("Обновление даты рождения");
            oldUser.setBirthday(user.getBirthday());
        }
        log.info("Информация об пользователе {} с id = {} обновлена", oldUser.getName(), oldUser.getId());
        return oldUser;
    }

    private void validateUser(User user) {
        log.info("Начало валидации данных user");
        if (user.getLogin().contains(" ")) { // проверяет, чтобы login вообще не содержал пробелы
            log.error("Ошибка валидации: Логин содержит пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        log.debug("Валидация имени");
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пустое поле name, вместо имени будет использован login = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        log.info("Валидация прошла успешно");
    }

    private void validateEmailDuplicates(User user) {
        log.info("Проверка на наличие дубликатов email");
        if (users.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            log.error("Ошибка валидации: email уже используется");
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        log.info("Дубликатов не найдено");
    }

    private Integer calcNextId() { // сделать простой вариант
        return ++count;
    }
}
