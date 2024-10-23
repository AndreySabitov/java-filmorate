package ru.yandex.practicum.filmorate.storage.review.like;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewLikeRowMapper implements RowMapper<ReviewLike> {
    @Override
    public ReviewLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewLike.builder()
                .reviewId(rs.getInt("review_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
