package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User getUserById(Integer id);

    User addUser(User user);

    User updateUser(User user);

    List<User> getUserFriends(Integer id);

    List<User> getMutualFriends(Integer id, Integer otherId);
}
