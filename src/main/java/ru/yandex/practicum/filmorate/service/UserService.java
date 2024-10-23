package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.filmLikes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.rating.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friendship.FriendshipStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipDbStorage;
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        if (user.getId() == null) {
            log.error("Ошибка валидации: не задан id");
            throw new ValidationException("id должен быть задан");
        }
        return userStorage.updateUser(user);
    }

    public User deleteUser(Integer userId) {
        User user = getUserById(userId);
        friendshipDbStorage.deleteFriendshipOfUser(userId);
        log.info("почистили дружбу пользователя");
        likeStorage.deleteLikesOfUser(userId);
        log.info("удалили лайки пользователя");
        userStorage.deleteUserById(userId);
        return user;
    }

    public User addFriend(Integer id, Integer friendId) {
        log.info("пользователь {} добавляет в друзья пользователя {}", id, friendId);
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendshipDbStorage.addFriend(id, friendId);
        return getUserById(id);
    }

    public User deleteFriend(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendshipDbStorage.deleteFriend(id, friendId);
        return getUserById(id);
    }

    public List<User> getUserFriends(Integer id) {
        User user = userStorage.getUserById(id);
        log.info("получаем список друзей пользователя {}", user);
        return userStorage.getUserFriends(id);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);
        log.info("оба пользователя есть в базе");
        return userStorage.getMutualFriends(id, otherId);
    }

    public List<Film> getRecommendedFilms(Integer id) {
        List<Film> recommendedFilms = new ArrayList<>();

        List<Integer> likesOfUser = likeStorage.getLikesOfUser(id);
        if (likesOfUser.isEmpty()) {
            return recommendedFilms;
        }
        log.info("нашли id фильмов, которые нравятся пользователю: {}", likesOfUser);

        Map<Integer, List<Integer>> likesOfUsers = new HashMap<>();
        likeStorage.getAllLikes().stream().filter(userLike -> !Objects.equals(userLike.getUserId(), id))
                .forEach(userLike -> {
                    if (!likesOfUsers.containsKey(userLike.getUserId())) {
                        likesOfUsers.put(userLike.getUserId(), List.of(userLike.getFilmId()));
                    } else {
                        List<Integer> filmsIds = new ArrayList<>(likesOfUsers.get(userLike.getUserId()));
                        filmsIds.add(userLike.getFilmId());
                        likesOfUsers.put(userLike.getUserId(), filmsIds);
                    }
                });

        if (likesOfUsers.isEmpty()) {
            return recommendedFilms;
        }

        Map<Integer, Integer> intersectionMap = new HashMap<>();
        likesOfUsers.keySet().forEach(i -> {
            int count = Math.toIntExact(likesOfUsers.get(i).stream().filter(likesOfUser::contains).count());
            if (count != 0) {
                intersectionMap.put(i, count);
            }
        });
        log.info("получили Map с пересечениями лайков с другими пользователями {}", intersectionMap);

        if (intersectionMap.isEmpty()) {
            return recommendedFilms;
        }

        Integer otherId = intersectionMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        likesOfUsers.get(otherId).stream().filter(i -> !likesOfUser.contains(i)).map(filmStorage::getFilmById)
                .forEach(film -> {
                    int filmId = film.getId();
                    film.getGenres().addAll(genreStorage.getGenresOfFilm(filmId));
                    film.setMpa(mpaStorage.getRatingOfFilm(filmId));
                    recommendedFilms.add(film);
                });

        return recommendedFilms;
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
}
