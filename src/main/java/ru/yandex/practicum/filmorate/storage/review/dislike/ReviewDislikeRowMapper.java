package ru.yandex.practicum.filmorate.storage.review.dislike;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewDislikeRowMapper implements RowMapper<ReviewDislike> {
    @Override
    public ReviewDislike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewDislike.builder()
                .reviewId(rs.getInt("review_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
