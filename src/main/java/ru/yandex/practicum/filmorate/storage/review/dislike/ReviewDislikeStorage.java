package ru.yandex.practicum.filmorate.storage.review.dislike;

import java.util.List;

public interface ReviewDislikeStorage {

    void addReviewDislike(Integer reviewId, Integer userId);

    void removeReviewDislike(Integer reviewId, Integer userId);

    List<ReviewDislike> getAllReviewDislikes(Integer id);

    void removeAllReviewDislikes(Integer id);
}
