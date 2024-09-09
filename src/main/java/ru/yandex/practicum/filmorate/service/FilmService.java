package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Integer id, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getIdsOfUsersLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", user.getName(), film.getName());
        return film;
    }

    public Film deleteLike(Integer id, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getIdsOfUsersLikes().remove(userId);
        log.info("Пользователь {} удалил лайк у фильма {}", user.getName(), film.getName());
        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> sortedFilms = new ArrayList<>(filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(film -> film.getIdsOfUsersLikes().size()))
                .toList());
        Collections.reverse(sortedFilms);
        List<Film> mostPopularFilms = new ArrayList<>();
        sortedFilms.forEach(film -> {
            if (mostPopularFilms.size() < count) {
                mostPopularFilms.add(film);
            }
        });
        log.info("Составлен список из {} самых популярных фильмов", count);
        return mostPopularFilms;
    }
}
