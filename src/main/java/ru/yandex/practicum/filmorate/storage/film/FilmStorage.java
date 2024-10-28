package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Integer id);

    int addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getFilmsByIdDirector(Integer dirId, String sortBy);

    void deleteFilm(Integer filmId);

    List<Film> getRecommendedFilms(Integer id);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getFilmsByNameDirector(String pattern);

    List<Film> getFilmsByTitle(String pattern);

    List<Film> getFilmsByTitleAndDirectorName(String pattern);
}
