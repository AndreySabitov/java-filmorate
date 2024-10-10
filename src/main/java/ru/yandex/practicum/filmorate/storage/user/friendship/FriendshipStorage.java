package ru.yandex.practicum.filmorate.storage.user.friendship;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<Friendship> getFriendship();

    List<Integer> getFriendshipOfUser(Integer id);
}
