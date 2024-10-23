package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.dislike.ReviewDislikeStorage;
import ru.yandex.practicum.filmorate.storage.review.like.ReviewLikeStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewLikeStorage reviewLikeStorage;
    private final ReviewDislikeStorage reviewDislikeStorage;

    public Review addNewReview(Review newReview) {
        try {
            return reviewStorage.addNewReview(newReview);
        } catch (Exception e) {
            log.error("Указанный id пользователя или фильма не найден. UserId={}, FilmId={}",
                    newReview.getUserId(),
                    newReview.getFilmId());
            throw new NotFoundException("Указанный id пользователя или фильма не найден.");
        }
    }

    public Review updateReview(Review newReview) {
        return reviewStorage.updateReview(newReview);
    }

    public void removeReview(Integer id) {
        reviewStorage.removeReview(id);
    }

    public Review getReviewById(Integer id) {
        try {
            return reviewStorage.getReviewById(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Отзыв под id=%s не найден", id));
        }
    }

    public List<Review> getAllReviewsByFilmId(Optional<Integer> filmId, int count) {
        return reviewStorage.getAllReviewsByFilmId(filmId, count);
    }

    public void addReviewLike(Integer reviewId, Integer userId) {
        reviewLikeStorage.addReviewLike(reviewId, userId);
    }

    public void removeReviewLike(Integer reviewId, Integer userId) {
        reviewLikeStorage.removeReviewLike(reviewId, userId);
    }

    public void addReviewDislike(Integer reviewId, Integer userId) {
        reviewDislikeStorage.addReviewDislike(reviewId, userId);
    }

    public void removeReviewDislike(Integer reviewId, Integer userId) {
        reviewDislikeStorage.removeReviewDislike(reviewId, userId);
    }
}
