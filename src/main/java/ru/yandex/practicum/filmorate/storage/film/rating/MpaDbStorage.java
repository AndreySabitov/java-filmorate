package ru.yandex.practicum.filmorate.storage.film.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
public class MpaDbStorage extends BaseDbStorage<MPA> implements MpaStorage {
    private static final String GET_ALL_RATINGS_QUERY = "SELECT * FROM rating";
    private static final String GET_RATING_BY_ID = "SELECT * FROM rating WHERE rating_id = ?";
    private static final String GET_RATING_OF_FILM_QUERY = "SELECT f.rating_id, r.rating_name FROM films f " +
            "JOIN rating r ON f.rating_id = r.rating_id WHERE film_id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<MPA> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<MPA> getRatings() {
        return findAll(GET_ALL_RATINGS_QUERY);
    }

    @Override
    public MPA getRatingById(Integer id) {
        return findOne(GET_RATING_BY_ID, id);
    }

    @Override
    public MPA getRatingOfFilm(Integer id) {
        return findOne(GET_RATING_OF_FILM_QUERY, id);
    }
}
