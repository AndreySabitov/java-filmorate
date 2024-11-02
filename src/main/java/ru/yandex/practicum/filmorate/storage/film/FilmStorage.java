package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.FilmSortBy;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Integer id);

    int addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getFilmsByIdDirector(Integer dirId, FilmSortBy sortBy);

    void deleteFilm(Integer filmId);

    List<Film> getRecommendedFilms(Integer id);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getMostPopularByGenreAndYear(Integer count, Integer genreId, Integer year);

    List<Film> getMostPopularByGenre(Integer count, Integer genreId);

    List<Film> getMostPopularByYear(Integer count, Integer year);

    List<Film> getFilmsByNameDirector(String pattern);

    List<Film> getFilmsByTitle(String pattern);

    List<Film> getFilmsByTitleAndDirectorName(String pattern);
}
