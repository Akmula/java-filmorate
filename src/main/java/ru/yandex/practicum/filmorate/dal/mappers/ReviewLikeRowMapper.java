package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewLikeRowMapper implements RowMapper<ReviewLike> {

    @Override
    public ReviewLike mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return ReviewLike.builder()
                .id(resultSet.getInt("id"))
                .reaction(resultSet.getInt("reaction"))
                .userId(resultSet.getInt("user_id"))
                .reviewId(resultSet.getInt("review_id"))
                .build();
    }
}