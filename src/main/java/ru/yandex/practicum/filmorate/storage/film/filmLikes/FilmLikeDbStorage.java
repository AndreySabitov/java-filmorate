package ru.yandex.practicum.filmorate.storage.film.filmLikes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
public class FilmLikeDbStorage extends BaseDbStorage<UserLikes> implements LikeStorage {
    private static final String ADD_LIKE_TO_FILM_QUERY = "INSERT INTO user_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM user_likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_LIKES_OF_ALL_FILMS = "SELECT f.film_id, ul.user_id FROM films f " +
            "LEFT JOIN user_likes ul ON f.film_id = ul.film_id";
    private static final String GET_IDS_OF_USERS_LIKES_QUERY = "SELECT user_id FROM user_likes WHERE film_id = ?";

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, RowMapper<UserLikes> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        update(ADD_LIKE_TO_FILM_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        update(DELETE_LIKE_QUERY, filmId, userId);
    }

    public List<UserLikes> getAllLikes() {
        return findAll(GET_LIKES_OF_ALL_FILMS);
    }

    @Override
    public List<Integer> getIdsOfUserLikes(Integer id) {
        return jdbcTemplate.queryForList(GET_IDS_OF_USERS_LIKES_QUERY, Integer.class, id);
    }
}
