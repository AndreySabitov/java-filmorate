package ru.yandex.practicum.filmorate.storage.review.like;

import java.util.List;

public interface ReviewLikeStorage {
    
    ReviewLike addReviewLike(Integer reviewId, Integer userId);

    void removeReviewLike(Integer reviewId, Integer userId);

    List<ReviewLike> getAllReviewLikes(Integer id);

    void removeAllReviewLikes(Integer id);
    
}
