package ru.yandex.practicum.filmorate.storage.review.dislike;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Primary
@Slf4j
@Component
@AllArgsConstructor
public class ReviewDislikeDbStorage implements ReviewDislikeStorage{

    private final JdbcTemplate jdbc;

    @Override
    public void addReviewDislike(Integer reviewId, Integer userId) {
        final String insert = "INSERT INTO review_dislikes (review_id, user_id) VALUES (?, ?)";
        final String decreaseUsefulness = "UPDATE reviews SET usefulness_rate = usefulness_rate - 1 WHERE id = ?";
        String message = "Ошибка добавления дизлайка к отзыву";
        boolean isSuccess = updater(insert, decreaseUsefulness, reviewId, userId);
        if (!isSuccess) {
            log.error(message);
        }
    }

    @Override
    public void removeReviewDislike(Integer reviewId, Integer userId) {
        final String delete = "DELETE FROM review_dislikes WHERE review_id=? AND user_id=?";
        final String increaseUsefulness = "UPDATE reviews SET usefulness_rate = usefulness_rate + 1 WHERE id = ?";
        String message = "Ошибка удаления дизлайка к отзыву";
        boolean isSuccess = updater(delete, increaseUsefulness, reviewId, userId);
        if (!isSuccess) {
            log.error(message);
        }
    }

    private boolean updater(String firstQuery, String secondQuery, Integer reviewId, Integer userId) {
        jdbc.update(firstQuery, reviewId, userId);
        jdbc.update(secondQuery, reviewId);
        return ((jdbc.update(firstQuery, reviewId, userId) > 0) && (jdbc.update(secondQuery, reviewId) > 0));
    }
}
