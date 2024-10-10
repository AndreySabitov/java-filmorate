package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.filmLikes.UserLikes;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserLikesMapper implements RowMapper<UserLikes> {
    @Override
    public UserLikes mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserLikes.builder()
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
