package ru.yandex.practicum.filmorate.storage.review.dislike;

public interface ReviewDislikeStorage {

    void addReviewDislike(Integer reviewId, Integer userId);

    void removeReviewDislike(Integer reviewId, Integer userId);
}
