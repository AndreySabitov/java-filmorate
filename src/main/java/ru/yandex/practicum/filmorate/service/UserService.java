package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Integer id, Integer friendId) {
        User userSender = userStorage.getUserById(id);
        User userRecipient = userStorage.getUserById(friendId);
        Set<Integer> senderFriendsIds = userSender.getFriendsIds();
        Set<Integer> recipientFriendsIds = userRecipient.getFriendsIds();
        senderFriendsIds.add(friendId);
        recipientFriendsIds.add(id);
        log.info("Пользователь {} добавил в друзья пользователя {}", userSender.getName(), userRecipient.getName());
        return userSender;
    }

    public User deleteFriend(Integer id, Integer friendId) {
        User userSender = userStorage.getUserById(id);
        User userRecipient = userStorage.getUserById(friendId);
        Set<Integer> senderFriendsIds = userSender.getFriendsIds();
        Set<Integer> recipientFriendsIds = userRecipient.getFriendsIds();
        senderFriendsIds.remove(friendId);
        recipientFriendsIds.remove(id);
        log.info("Пользователь {} удалил из друзей пользователя {}", userSender.getName(), userRecipient.getName());
        return userSender;
    }

    public List<User> getUserFriends(Integer id) {
        User user = userStorage.getUserById(id);
        Set<Integer> userFriendsIds = user.getFriendsIds();
        List<User> userFriends = new ArrayList<>();
        userFriendsIds.forEach(integer -> userFriends.add(userStorage.getUserById(integer)));
        log.info("Получен список друзей пользователя {}", user.getName());
        return userFriends;
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        List<User> senderFriends = getUserFriends(id);
        List<User> recipientFriends = getUserFriends(otherId);
        List<User> mutualFriends = new ArrayList<>();
        senderFriends.forEach(user -> {
            if (recipientFriends.contains(user)) {
                mutualFriends.add(user);
            }
        });
        log.info("Получен список общих друзей пользователей {} и {}", userStorage.getUserById(id),
                userStorage.getUserById(otherId));
        return mutualFriends;
    }
}
