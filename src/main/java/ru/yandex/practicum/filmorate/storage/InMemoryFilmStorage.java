package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer count = 0;

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error("Фильм с таким id не найден");
            throw new NotFoundException("Фильм с таким id не найден");
        }
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        validateFilmNameDuplicate(film);
        film.setId(calcNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм {}", film.getName());
        return film;
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
        return films.values().stream().anyMatch(film -> film.getName().equals(newFilm.getName()));
    }
}
