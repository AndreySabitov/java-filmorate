package ru.yandex.practicum.filmorate.storage.review.mark;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ReviewMarkDbStorage implements ReviewMarkStorage {

    private static final String INSERT_QUERY = "INSERT INTO review_mark (review_id, user_id, is_positive) " +
            "VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE review_mark WHERE review_id=?, user_id=?";
    private static final String INCREASE_USEFULNESS_QUERY = "UPDATE reviews " +
            "SET usefulness_rate = usefulness_rate + 1 WHERE id = ?";
    private static final String DECREASE_USEFULNESS_QUERY = "UPDATE reviews " +
            "SET usefulness_rate = usefulness_rate - 1 WHERE id = ?";

    private final JdbcTemplate jdbc;
    private final RowMapper<ReviewMark> mapper;

    @Override
    public void addReviewLike(Integer reviewId, Integer userId) {
        if (checkMarkByUserId(reviewId, userId, true)) {
            removeReviewDislike(reviewId, userId);
        }
        inserter(reviewId, userId, true, INCREASE_USEFULNESS_QUERY);
    }

    @Override
    public void addReviewDislike(Integer reviewId, Integer userId) {
        if (checkMarkByUserId(reviewId, userId, false)) {
            removeReviewLike(reviewId, userId);
        }
        inserter(reviewId, userId, false, DECREASE_USEFULNESS_QUERY);
    }

    @Override
    public void removeReviewLike(Integer reviewId, Integer userId) {
        remover(reviewId, userId, DECREASE_USEFULNESS_QUERY);
    }

    @Override
    public void removeReviewDislike(Integer reviewId, Integer userId) {
        remover(reviewId, userId, INCREASE_USEFULNESS_QUERY);
    }

    @Override
    public boolean checkMarkByUserId(Integer reviewId, Integer userId, boolean isPositive) {
        log.info("Проверка на наличее пересекающейся оценки...");
        final String checkMark = "SELECT * FROM review_mark WHERE review_id=? AND user_id=? AND is_positive NOT IN ?";
        try {
            return jdbc.queryForObject(checkMark, mapper, reviewId, userId, isPositive) != null;
        } catch (Exception e) {
            log.trace("Пересечения не найдено.");
            return false;
        }
    }

    private void inserter(Integer reviewId, Integer userId, boolean isPositive, String secondaryQuery) {
        boolean isSuccess = updater(INSERT_QUERY,
                                    secondaryQuery,
                                    reviewId,
                                    userId,
                                    Optional.of(isPositive));
        if (!isSuccess) {
            log.error("Ошибка добавления оценки отзыва");
        }
    }

    private void remover(Integer reviewId, Integer userId, String secondaryQuery) {
        boolean isSuccess = updater(DELETE_QUERY,
                                    secondaryQuery,
                                    reviewId,
                                    userId,
                            Optional.empty());
        if (!isSuccess) {
            log.error("Ошибка удаления оценки отзыва");
        }
    }

    private boolean updater(String primaryQuery,
                            String secondaryQuery,
                            Integer reviewId,
                            Integer userId,
                            Optional<Boolean> isPositive) {
        return isPositive
                .map(aBoolean -> ((jdbc.update(primaryQuery, reviewId, userId, aBoolean) > 0) &&
                        (jdbc.update(secondaryQuery, reviewId) > 0)))
                .orElseGet(() -> ((jdbc.update(primaryQuery, reviewId, userId) > 0) &&
                        (jdbc.update(secondaryQuery, reviewId) > 0)));
    }
}
