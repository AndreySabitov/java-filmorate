package ru.yandex.practicum.filmorate.storage.film.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {
    private static final String GET_DIRECTORS_OF_FILM_QUERY = "SELECT d.director_id, director_name FROM films_directors " +
            "AS fd JOIN directors AS d ON fd.director_id = d.director_id WHERE film_id = ?";
    private static final String GET_ALL_DIRECTORS_QUERY = "SELECT * FROM directors";
    private static final String GET_DIRECTOR_BY_ID_QUERY = "SELECT * FROM directors WHERE director_id = ?";
    private static final String ADD_DIRECTOR_QUERY = "INSERT INTO directors (director_name) VALUES (?)";
    private static final String DELETE_DIRECTOR_OF_FILM_QUERY = "DELETE FROM films_directors WHERE film_id = ?";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET director_name = ? WHERE director_id = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM directors WHERE director_id = ?";

    public DirectorDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Director> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Director> getDirectors() {
        return findAll(GET_ALL_DIRECTORS_QUERY);
    }

    @Override
    public Director getDirectorById(Integer dirId) {
        return findOne(GET_DIRECTOR_BY_ID_QUERY, dirId);
    }

    @Override
    public int addDirector(Director director) {
        return insert(ADD_DIRECTOR_QUERY, director.getName());
    }

    @Override
    public void updateDirector(Director director) {
        update(UPDATE_DIRECTOR_QUERY, director.getName(), director.getId());
    }

    @Override
    public void deleteDirector(Integer dirId) {
        delete(DELETE_DIRECTOR_QUERY, dirId);
    }

    @Override
    public List<Director> getDirectorsOfFilm(Integer filmId) {
        return findAll(GET_DIRECTORS_OF_FILM_QUERY, filmId);
    }

    @Override
    public void deleteDirectorOfFilm(Integer filmId) {
        delete(DELETE_DIRECTOR_OF_FILM_QUERY, filmId);
    }
}
