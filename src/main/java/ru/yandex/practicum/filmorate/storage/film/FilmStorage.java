package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Integer id);

    int addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getMostPopularFilms(Integer count);
}
