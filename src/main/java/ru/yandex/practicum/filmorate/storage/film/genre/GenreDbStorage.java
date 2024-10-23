package ru.yandex.practicum.filmorate.storage.film.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genre ";
    private static final String GET_GENRE_BY_ID_QUERY = GET_ALL_GENRES_QUERY.concat("WHERE genre_id = ?");
    private static final String GET_GENRES_OF_FILM = "SELECT g.genre_id, genre_name FROM films_genres fg " +
            "JOIN genre g ON fg.genre_id = g.genre_id WHERE film_id = ?";
    private static final String DELETE_GENRES_OF_FILM = "DELETE FROM films_genres WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Genre> getGenres() {
        return findAll(GET_ALL_GENRES_QUERY);
    }

    @Override
    public Genre getGenreById(Integer id) {
        return findOne(GET_GENRE_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getGenresOfFilm(Integer id) {
        return findAll(GET_GENRES_OF_FILM, id);
    }

    public void deleteGenresOfFilm(Integer filmId) {
        delete(DELETE_GENRES_OF_FILM, filmId);
    }
}
