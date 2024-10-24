package ru.yandex.practicum.filmorate.storage.film.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> getDirectors();

    List<Director> getDirectorsOfFilm(Integer filmId);

    void deleteDirectorOfFilm(Integer filmId);

    Director getDirectorById(Integer dirId);

    int addDirector(Director director);

    void updateDirector(Director director);

    void deleteDirector(Integer dirId);

}
