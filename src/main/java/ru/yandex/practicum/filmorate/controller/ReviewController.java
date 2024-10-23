package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public List<Review> getReviewsByFilmId(@RequestParam Optional<Integer> filmId,
                                           @RequestParam(defaultValue = "10") int count) {

        return service.getAllReviewsByFilmId(filmId, count);
    }

    @PutMapping(LIKE_PATH)
    public void addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addReviewLike(id, userId);
    }

    @PutMapping(DISLIKE_PATH)
    public void addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addReviewDislike(id, userId);
    }

    @DeleteMapping(LIKE_PATH)
    public void removeLikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.removeReviewLike(id, userId);
    }

    @DeleteMapping(DISLIKE_PATH)
    public void removeDislikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        service.removeReviewDislike(id, userId);
    }
}
