package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.history.HistoryDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.mark.ReviewMarkStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewMarkStorage reviewMarkStorage;
    private final UserStorage userStorage;
    private final HistoryDbStorage historyDbStorage;

    public Review addNewReview(Review newReview) {
        try {
            validateFields(newReview);
            Review addedReview = reviewStorage.addNewReview(newReview);
            historyDbStorage.saveHistoryEvent(
                    newReview.getUserId(),
                    System.currentTimeMillis(),
                    EventType.REVIEW,
                    OperationType.ADD,
                    addedReview.getReviewId());
            log.info("Отзыв добавлен в историю: добавлен новый отзыв с id {}", addedReview.getReviewId());
            return addedReview;
        } catch (Exception e) {
            log.error("Указанный id пользователя или фильма не найден. UserId={}, FilmId={}",
                    newReview.getUserId(),
                    newReview.getFilmId());
            throw new NotFoundException("Указанный id пользователя или фильма не найден.");
        }
    }

    public Review updateReview(Review newReview) {
        log.info("Обновление отзыва.");
        validateFields(newReview);
        Review review = reviewStorage.updateReview(newReview);
        historyDbStorage.saveHistoryEvent(
                review.getUserId(),
                System.currentTimeMillis(),
                EventType.REVIEW,
                OperationType.UPDATE,
                review.getReviewId());
        log.info("Отзыв изменен в истории: изменен отзыв с id {}", review.getReviewId());
        return review;
    }

    public void removeReview(Integer id) {
        log.info("Удаление отзыва.");
        Integer userId = getReviewById(id).getUserId();
        reviewStorage.removeReview(id);
        historyDbStorage.saveHistoryEvent(
                userId,
                System.currentTimeMillis(),
                EventType.REVIEW,
                OperationType.REMOVE,
                id);
        log.info("Отзыв удален из истории: удален отзыв с id {}", id);
    }

    public Review getReviewById(Integer id) {
        try {
            return reviewStorage.getReviewById(id);
        } catch (Exception e) {
            log.info("Отзыв под id={} не найден", id);
            throw new NotFoundException(String.format("Отзыв под id=%s не найден", id));
        }
    }

    public List<Review> getReviews(Optional<Integer> filmId, int count) {
        if (filmId.isPresent()) {
            return reviewStorage.getAllReviewsByFilmId(filmId.get(), count);
        } else {
            return reviewStorage.getAllReviews();
        }
    }

    public void addReviewMark(Integer reviewId, Integer userId, boolean isPositive) {
        verifier(reviewId, userId);
        if (isPositive) {
            log.info("Добавление лайка.");
            reviewMarkStorage.addReviewLike(reviewId, userId);
        } else {
            log.info("Добавление дизлайка.");
            reviewMarkStorage.addReviewDislike(reviewId, userId);
        }
    }

    public void removeReviewMark(Integer reviewId, Integer userId, boolean isPositive) {
        verifier(reviewId, userId);
        if (isPositive) {
            log.info("Удаление лайка.");
            reviewMarkStorage.removeReviewLike(reviewId, userId);
        } else {
            log.info("Удаление дизлайка.");
            reviewMarkStorage.removeReviewDislike(reviewId, userId);
        }
    }

    //Костыль, лучше не трогать
    private void validateFields(Review review) {
        if (review.getUserId() <= 0) {
            throw new NotFoundException("Id пользователя должен быть положительным");
        } else if (review.getFilmId() <= 0) {
            throw new NotFoundException("Id фильма должен быть положительным");
        }
    }

    //Проверка на наличее юзера и отзыва под указанными id
    private void verifier(Integer reviewId, Integer userId) {
        getReviewById(reviewId);
        try {
            userStorage.getUserById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(String.format("Пользователь c id=%s не найден", userId));
        }
    }
}
