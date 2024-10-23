package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review addNewReview(Review newReview);

    Review updateReview(Review updatedReview);

    void removeReview(Integer id);

    List<Review> getAllReviewsByFilmId(Optional<Integer> filmId, int count);

    Review getReviewById(Integer reviewId);
}
