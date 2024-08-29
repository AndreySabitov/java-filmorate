package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer count = 0;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        validateUser(user);
        validateEmailDuplicates(user);
        user.setId(calcNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateUser(user);
        Integer id = user.getId();
        if (id == null) {
            log.error("Ошибка валидации: не задан id");
            throw new ConditionsNotMetException("id должен быть задан");
        }
        if (users.containsKey(id)) {
            User oldUser = users.get(id);
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
        } else {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    private void validateUser(User user) {
        log.info("Начало валидации данных user");
        if (user.getLogin().contains(" ")) { // проверяет, чтобы login вообще не содержал пробелы
            log.error("Ошибка валидации: Логин содержит пробелы");
            throw new ConditionsNotMetException("Логин не может содержать пробелы");
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
