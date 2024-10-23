package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review addNewReview(Review newReview);

    Review updateReview(Review updatedReview);

    void removeReview(Integer id);

    List<Review> getAllReviewsByFilmId(Integer filmId, int count);
}
