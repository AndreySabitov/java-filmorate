package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<String, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return users.values().stream().sorted(Comparator.comparing(User::getId)).toList();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Начало валидации нового пользователя");
        log.debug("Валидация email");
        if (user.getEmail() == null) {
            log.info("Ошибка валидации: Не задан email");
            throw new ConditionsNotMetException("email должен быть задан");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Ошибка валидации: email не содержит символ @");
            throw new ConditionsNotMetException("email должен содержать символ @");
        }
        if (users.containsKey(user.getEmail())) {
            log.info("Ошибка валидации: email уже используется");
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        log.debug("Валидация логина");
        if (user.getLogin() == null) {
            log.info("Ошибка валидации: Логин не задан");
            throw new ConditionsNotMetException("Логин должен быть задан");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Ошибка валидации: Логин пустой или содержит пробелы");
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }
        log.debug("Валидация имени");
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Ошибка валидации: пустое поле name, вместо имени будет использован login {}", user.getLogin());
            user.setName(user.getLogin());
        }
        log.debug("Валидация даты рождения");
        if (user.getBirthday() == null) {
            log.info("Ошибка валидации: Не задана дата рождения");
            throw new ConditionsNotMetException("Дата рождения должна быть задана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Ошибка валидации: Дата рождения не может быть в будущем");
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        user.setId(calcNextId());
        users.put(user.getEmail(), user);
        log.info("Валидация прошла успешно. Новый пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            log.info("Ошибка валидации: не задан id");
            throw new ConditionsNotMetException("id должен быть задан");
        }
        Optional<User> optionalUser = users.values().stream()
                .filter(user1 -> user1.getId().equals(user.getId()))
                .findFirst();
        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();
            log.info("Начало обновления информации о пользователе {}", oldUser.getName());
            if (user.getEmail() != null) {
                oldUser.setEmail(user.getEmail());
            }
            if (user.getLogin() != null) {
                oldUser.setLogin(user.getLogin());
            }
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
            if (user.getBirthday() != null) {
                oldUser.setBirthday(user.getBirthday());
            }
            log.info("Информация об пользователе с id = {} обновлена", oldUser.getId());
            return oldUser;
        } else {
            log.info("Пользователь не найден");
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    private Long calcNextId() {
        Long result = users.values().stream()
                .map(User::getId)
                .max(Long::compareTo)
                .orElse(0L);
        return ++result;
    }
}
