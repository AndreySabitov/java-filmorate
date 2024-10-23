package ru.yandex.practicum.filmorate.storage.review.like;

import java.util.List;

public interface ReviewLikeStorage {
    
    void addReviewLike(Integer reviewId, Integer userId);

    void removeReviewLike(Integer reviewId, Integer userId);
    
}
