package ru.yandex.practicum.filmorate.storage.review.dislike;

import java.util.List;

public interface ReviewDislikeStorage {

    void addReviewDislike(Integer reviewId, Integer userId);

    void removeReviewDislike(Integer reviewId, Integer userId);
}
