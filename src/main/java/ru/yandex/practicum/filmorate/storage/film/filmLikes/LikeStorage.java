package ru.yandex.practicum.filmorate.storage.film.filmLikes;

import java.util.List;

public interface LikeStorage {
    void addLike(Integer userId, Integer filmId);

    void deleteLike(Integer filmId, Integer userId);

    List<UserLikes> getAllLikes();

    List<Integer> getIdsOfUserLikes(Integer id);

    List<Integer> getLikesOfUser(Integer id);
}
