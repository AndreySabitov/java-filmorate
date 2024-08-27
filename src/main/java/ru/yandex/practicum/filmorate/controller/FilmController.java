package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Начало валидации информации о добавляемом фильме");
        log.debug("Валидация названия фильма");
        if (film.getName() == null) {
            log.info("Ошибка валидации: название не задано");
            throw new ConditionsNotMetException("Название фильма должно быть задано");
        }
        if (film.getName().isBlank()) {
            log.info("Ошибка валидации названия фильма");
            throw new ConditionsNotMetException("Некорректное название фильма");
        }
        if (checkNameDuplicate(film)) {
            log.info("Ошибка валидации названия фильма");
            throw new DuplicateException("Фильм с таким названием уже существует");
        }
        log.debug("Валидация описания фильма");
        if (film.getDescription().length() > 200) {
            log.info("Ошибка валидации описания фильма");
            throw new ConditionsNotMetException("Длина описания фильма не должна превышать 200 символов");
        }
        log.debug("Валидация даты релиза");
        if (film.getReleaseDate() == null) {
            log.info("Ошибка валидации даты релиза");
            throw new ConditionsNotMetException("Дата релиза должна быть задана");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Ошибка валидации даты релиза");
            throw new ConditionsNotMetException("Дата релиза должна быть не раньше 28.12.1895г");
        }
        log.debug("Валидация продолжительности фильма");
        if (film.getDuration() == null) {
            log.info("Ошибка валидации продолжительности фильма");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть задана");
        }
        if (film.getDuration() < 0) {
            log.info("Ошибка валидации продолжительности фильма");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительной");
        }
        film.setId(calcNextId());
        films.put(film.getId(), film);
        log.info("Валидация прошла успешно. Добавлен новый фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            log.info("Не задан id");
            throw new ConditionsNotMetException("id должен быть задан!");
        }
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            log.info("Начало обновления информации о фильме {}", oldFilm.getName());
            if (film.getName() != null) {
                log.debug("Обновление названия");
                oldFilm.setName(film.getName());
            }
            if (film.getDescription() != null) {
                log.debug("Обновление описания");
                oldFilm.setDescription(film.getDescription());
            }
            if (film.getReleaseDate() != null) {
                log.debug("Обновление даты релиза");
                oldFilm.setReleaseDate(film.getReleaseDate());
            }
            if (film.getDuration() != null) {
                log.debug("Обновление информации о продолжительности фильма");
                oldFilm.setDuration(film.getDuration());
            }
            log.info("Информация о фильме успешно обновлена");
            return oldFilm;
        } else {
            log.info("Не верно задан id");
            throw new NotFoundException("Фильм с таким id не найден");
        }
    }

    private Long calcNextId() {
        Long result = films.values().stream()
                .map(Film::getId)
                .max(Long::compareTo)
                .orElse(0L);
        return ++result;
    }

    private boolean checkNameDuplicate(Film newFilm) {
        return films.values().stream().anyMatch(film -> film.getName().equals(newFilm.getName()));
    }
}
