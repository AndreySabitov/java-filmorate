package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.review.mark.ReviewMark;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewMarkRowMapper implements RowMapper<ReviewMark> {
    @Override
    public ReviewMark mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewMark.builder()
                .reviewId(rs.getInt("review_id"))
                .userId(rs.getInt("user_id"))
                .isPositive(rs.getBoolean("is_positive"))
                .build();
    }
}
