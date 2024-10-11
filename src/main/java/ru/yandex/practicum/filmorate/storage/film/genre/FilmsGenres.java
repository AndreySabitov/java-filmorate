package ru.yandex.practicum.filmorate.storage.film.genre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmsGenres {
    private final Integer filmId;
    private final Integer genreId;
}
