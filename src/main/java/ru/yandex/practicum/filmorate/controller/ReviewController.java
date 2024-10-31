package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private static final String LIKE_PATH = "/{id}/like/{userId}";
    private static final String DISLIKE_PATH = "/{id}/dislike/{userId}";

    private final ReviewService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@Valid @RequestBody Review review) {
        return service.addNewReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return service.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable Integer id) {
        service.removeReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        return service.getReviewById(id);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam Optional<Integer> filmId,
                                   @RequestParam(defaultValue = "10") int count) {

        return service.getReviews(filmId, count);
    }

    @PutMapping(LIKE_PATH)
    public void addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addReviewMark(id, userId, true);
    }

    @PutMapping(DISLIKE_PATH)
    public void addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addReviewMark(id, userId, false);
    }

    @DeleteMapping(LIKE_PATH)
    public void removeLikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.removeReviewMark(id, userId, true);
    }

    @DeleteMapping(DISLIKE_PATH)
    public void removeDislikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.removeReviewMark(id, userId, false);
    }
}
