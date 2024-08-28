package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<String, User> users = new HashMap<>();
    private Integer count = 0;

    @GetMapping
    public List<User> getUsers() {
        return users.values().stream().sorted(Comparator.comparing(User::getId)).toList();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Начало валидации нового пользователя");
        if (users.containsKey(user.getEmail())) {
            log.error("Ошибка валидации: email уже используется");
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: Логин содержит пробелы");
            throw new ConditionsNotMetException("Логин не может содержать пробелы");
        }
        log.debug("Валидация имени");
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пустое поле name, вместо имени будет использован login = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        user.setId(calcNextId());
        users.put(user.getEmail(), user);
        log.info("Валидация прошла успешно. Новый пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        Integer id = user.getId();
        if (id == null) {
            log.error("Ошибка валидации: не задан id");
            throw new ConditionsNotMetException("id должен быть задан");
        }
        Optional<User> optionalUser = users.values().stream()
                .filter(user1 -> user1.getId().equals(id))
                .findFirst();
        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();
            log.info("Начало обновления информации о пользователе {}", oldUser.getName());
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                log.debug("Обновление email");
                oldUser.setEmail(user.getEmail());
            }
            if (user.getLogin() != null && !user.getLogin().isBlank()) {
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

    private Integer calcNextId() { // сделать простой вариант
        return ++count;
    }
}
