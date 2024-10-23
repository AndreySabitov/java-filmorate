package ru.yandex.practicum.filmorate.storage.review.like;

public interface ReviewLikeStorage {
    
    void addReviewLike(Integer reviewId, Integer userId);

    void removeReviewLike(Integer reviewId, Integer userId);

    boolean checkLikeByUserId(Integer reviewId, Integer userId);
}
