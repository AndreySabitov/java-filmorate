package ru.yandex.practicum.filmorate.storage.film.rating;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {
    List<MPA> getRatings();

    MPA getRatingById(Integer id);

    MPA getRatingOfFilm(Integer id);
}
