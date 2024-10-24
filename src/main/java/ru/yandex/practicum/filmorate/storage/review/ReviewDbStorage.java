package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ReviewDbStorage extends BaseDbStorage<Review> implements ReviewStorage {

    private static final String GET_ALL_QUERY = "SELECT * FROM reviews";
    private static final String ADD_NEW_REVIEW_QUERY = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
            "VALUES (?,?,?,?)";
    private static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content=?, is_positive=?, user_id=?, " +
            " film_id=? WHERE id=?";
    private static final String DELETE_REVIEW_QUERY = "DELETE FROM reviews WHERE id=?";

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Review> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Review addNewReview(Review newReview) {
        log.info("добавление отзыва...");
        int generatedId = insert(
                ADD_NEW_REVIEW_QUERY,
                newReview.getContent(),
                newReview.getIsPositive(),
                newReview.getUserId(),
                newReview.getFilmId()
        );
        log.trace("отзыву присвоен id={}", generatedId);
        log.info("отзыв добавлен");
        return newReview.toBuilder().reviewId(generatedId).build();
    }

    @Override
    public Review updateReview(Review updatedReview) {
        log.info("обновление данных...");
        try {
            update(
                    UPDATE_REVIEW_QUERY,
                    updatedReview.getContent(),
                    updatedReview.getIsPositive(),
                    updatedReview.getUserId(),
                    updatedReview.getFilmId(),
                    updatedReview.getReviewId()
            );
        } catch (Exception e) {
            log.error("отзыв под id={} не найден", updatedReview.getReviewId());
            throw new NotFoundException(String.format("отзыв под id=%s не найден", updatedReview.getReviewId()));
        }
        log.info("отзыв под id {} обновлен", updatedReview.getReviewId());
        return updatedReview;
    }

    @Override
    public void removeReview(Integer id) {
        log.info("Удаление отзыва под id={}", id);
        try {
            delete(DELETE_REVIEW_QUERY, id);
        } catch (Exception e) {
            log.error("отзыв под id={} не найден", id);
            throw new NotFoundException(String.format("отзыв под id=%s не найден", id));
        }
        log.info("отзыв удален");
    }

    @Override
    public List<Review> getAllReviewsByFilmId(Integer filmId, int count) {
        final String getReviewsByFilmIdLimited = GET_ALL_QUERY.concat(" WHERE film_id=? " +
                "ORDER BY usefulness_rate DESC LIMIT ?");
        try {
            log.info("Получение списка из {} отзывов фильма под id={}", count, filmId);
            return findAll(
                    getReviewsByFilmIdLimited,
                    filmId,
                    count
            );
        } catch (NotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Review> getAllReviews() {
        try {
            log.info("Получение списка всех отзывов");
            return findAll(GET_ALL_QUERY.concat(" ORDER BY usefulness_rate DESC"));
        } catch (NotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        final String getReviewById = GET_ALL_QUERY.concat(" WHERE id = ?");
        return findOne(getReviewById, reviewId);
    }
}
