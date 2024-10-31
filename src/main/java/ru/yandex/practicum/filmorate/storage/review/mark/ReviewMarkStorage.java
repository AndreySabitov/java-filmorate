package ru.yandex.practicum.filmorate.storage.review.mark;

public interface ReviewMarkStorage {

    void addReviewLike(Integer reviewId, Integer userId);

    void addReviewDislike(Integer reviewId, Integer userId);

    void removeReviewLike(Integer reviewId, Integer userId);

    void removeReviewDislike(Integer reviewId, Integer userId);

    boolean checkMarkByUserId(Integer reviewId, Integer userId, boolean isPositive);
}
