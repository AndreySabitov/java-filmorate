package ru.yandex.practicum.filmorate.storage.review.dislike;

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
public class ReviewDislike {
    @NotNull
    Integer filmId;
    @NotNull
    Integer userId;
}
