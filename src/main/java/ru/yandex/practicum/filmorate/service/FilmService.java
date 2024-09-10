package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Integer count = 0;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        validateFilmNameDuplicate(film);
        film.setId(calcNextId());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        Integer id = film.getId();
        if (id == null) {
            log.error("Не задан id");
            throw new ValidationException("id должен быть задан!");
        }
        Film oldFilm = getFilmById(id);
        log.info("Начало обновления информации о фильме {}", oldFilm.getName());
        if (!oldFilm.getName().equals(film.getName())) {
            log.debug("Обновление названия");
            oldFilm.setName(film.getName());
        }
        if (film.getDescription() != null) {
            log.debug("Обновление описания");
            oldFilm.setDescription(film.getDescription());
        }
        log.debug("Обновление даты релиза");
        oldFilm.setReleaseDate(film.getReleaseDate());
        if (film.getDuration() != null) {
            log.debug("Обновление информации о продолжительности фильма");
            oldFilm.setDuration(film.getDuration());
        }
        log.info("Информация о фильме {} успешно обновлена", oldFilm.getName());
        return oldFilm;
    }

    public Film addLike(Integer id, Integer userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getIdsOfUsersLikes().add(userId);
        return film;
    }

    public Film deleteLike(Integer id, Integer userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getIdsOfUsersLikes().remove(userId);
        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return new ArrayList<>(filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(film -> film.getIdsOfUsersLikes().size()))
                .toList()).reversed().stream()
                .limit(count)
                .toList();
        /* не смог перевернуть сортировку в стриме: если добавляю .reversed() к компаратору,
         то у film остаются доступны только методы класса Object */
    }

    private void validateFilm(Film film) {
        log.info("Начало валидации информации о добавляемом фильме");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации:Дата релиза должна быть не раньше 28.12.1895г");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895г");
        }
        log.info("Валидация прошла успешно");
    }

    private void validateFilmNameDuplicate(Film film) {
        log.info("Проверка уникальности названия фильма");
        if (checkNameDuplicate(film)) {
            log.error("Ошибка валидации: фильм {} уже существует", film.getName());
            throw new DuplicateException("Фильм с таким названием уже существует");
        }
        log.info("Проверка прошла успешно");
    }

    private Integer calcNextId() {
        return ++count;
    }

    private boolean checkNameDuplicate(Film newFilm) {
        return filmStorage.getFilms().stream().anyMatch(film -> film.getName().equals(newFilm.getName()));
    }
}
