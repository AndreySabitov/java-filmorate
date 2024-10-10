package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.film.genre.FilmsGenres;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmsGenresRowMapper implements RowMapper<FilmsGenres> {
    @Override
    public FilmsGenres mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmsGenres.builder()
                .filmId(rs.getInt("film_id"))
                .genreId(rs.getInt("genre_id"))
                .build();
    }
}
