package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private Integer count = 0;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        validateUser(user);
        validateEmailDuplicates(user);
        user.setId(calcNextId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        if (user.getId() == null) {
            log.error("Ошибка валидации: не задан id");
            throw new ValidationException("id должен быть задан");
        }
        User oldUser = getUserById(user.getId());
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

    public User addFriend(Integer id, Integer friendId) {
        userStorage.getUserById(id).getFriendsIds().add(friendId);
        userStorage.getUserById(friendId).getFriendsIds().add(id);
        return userStorage.getUserById(id);
    }

    public User deleteFriend(Integer id, Integer friendId) {
        userStorage.getUserById(id).getFriendsIds().remove(friendId);
        userStorage.getUserById(friendId).getFriendsIds().remove(id);
        return userStorage.getUserById(id);
    }

    public List<User> getUserFriends(Integer id) {
        return userStorage.getUserById(id).getFriendsIds().stream()
                .map(userStorage::getUserById)
                .toList();
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        Set<Integer> recipientFriendsIds = userStorage.getUserById(otherId).getFriendsIds();
        return userStorage.getUserById(id).getFriendsIds().stream()
                .filter(recipientFriendsIds::contains)
                .map(userStorage::getUserById)
                .toList();
    }

    private void validateUser(User user) {
        log.info("Начало валидации данных user");
        if (user.getLogin().contains(" ")) {
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
        if (userStorage.getUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            log.error("Ошибка валидации: email уже используется");
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        log.info("Дубликатов не найдено");
    }

    private Integer calcNextId() {
        return ++count;
    }
}
