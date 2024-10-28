package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Integer id);

    int addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getFilmsByDirector(Integer dirId, String sortBy);

    void deleteFilm(Integer filmId);

    List<Film> getRecommendedFilms(Integer id);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getMostPopularByGenreAndYear(Integer count, String queryCondition);
}
