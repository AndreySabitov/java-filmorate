package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
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
public class Review {
    Integer reviewId;
    @NotBlank(message = "Отзыв не может быть пустым. Вы что, бот?")
    String content;
    boolean isPositive;
    @NotNull(message = "Не указан id пользователя")
    Integer userId;
    @NotNull(message = "Не указан id фильма")
    Integer filmId;
    Integer useful;
}
