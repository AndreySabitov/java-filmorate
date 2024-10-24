package ru.yandex.practicum.filmorate.storage.review.like;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewLike {
    @NotNull
    Integer reviewId;
    @NotNull
    Integer userId;
}
