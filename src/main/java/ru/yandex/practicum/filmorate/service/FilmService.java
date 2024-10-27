package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.filmLikes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.rating.MpaStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreDbStorage;
    private final DirectorStorage directorDbStorage;
    private final MpaStorage mpaStorage;

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        films.forEach(this::setFields);
        return films;
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        setFields(film);
        return film;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        int id = filmStorage.addFilm(film);
        return getFilmById(id);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        Integer id = film.getId();
        if (id == null) {
            log.error("Не задан id");
            throw new ValidationException("id должен быть задан!");
        }
        filmStorage.updateFilm(film);
        return getFilmById(id);
    }

    public Film deleteFilm(Integer filmId) {
        Film film = getFilmById(filmId);
        filmStorage.deleteFilm(filmId);
        return film;
    }

    public Film addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
        return getFilmById(id);
    }

    public Film deleteLike(Integer id, Integer userId) {
        likeStorage.deleteLike(id, userId);
        return getFilmById(id);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> films = filmStorage.getMostPopularFilms(count);
        films.forEach(this::setFields);
        return films;
    }

    public List<Film> getFilmsByDirector(Integer dirId, String sortBy) {
        List<Film> films = filmStorage.getFilmsByDirector(dirId, sortBy);
        films.forEach(this::setFields);
        return films;
    }

    public List<Film> getFilmsBySubstring(String query, String searchBy) {
        List<Film> films = filmStorage.getFilmsBySubstring(query, searchBy);
        films.forEach(this::setFields);
        return films;
    }

    private void setFields(Film film) {
        int id = film.getId();
        film.getGenres().addAll(genreDbStorage.getGenresOfFilm(id));
        film.setMpa(mpaStorage.getRatingOfFilm(id));
        film.getDirectors().addAll(directorDbStorage.getDirectorsOfFilm(id));
    }

    private void validateFilm(Film film) {
        log.info("Начало валидации информации о добавляемом фильме");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации:Дата релиза должна быть не раньше 28.12.1895г");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895г");
        }
        try {
            film.getGenres().stream().map(Genre::getId).forEach(genreDbStorage::getGenreById);
        } catch (NotFoundException e) {
            throw new ValidationException("некорректно задан id жанра");
        }
        try {
            mpaStorage.getRatingById(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ValidationException("задан некорректный id MPA");
        }
        log.info("Валидация прошла успешно");
    }
}
