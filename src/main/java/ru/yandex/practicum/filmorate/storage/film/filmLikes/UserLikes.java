package ru.yandex.practicum.filmorate.storage.film.filmLikes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLikes {
    private final Integer filmId;
    private final Integer userId;
}
