package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<MPA> {

    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .build();
    }
}
