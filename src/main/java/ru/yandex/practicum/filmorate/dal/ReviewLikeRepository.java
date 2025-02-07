package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.ReviewLike;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;

@Slf4j
@Repository
public class ReviewLikeRepository extends BaseRepository<ReviewLike> implements ReviewLikeStorage {

    private static final int positiveReaction = 1;
    private static final int negativeReaction = -1;

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO REVIEW_LIKES (reaction, review_id, user_id)
            VALUES (?, ?, ?)
            """;

    private static final String GET_LIKE_ID_QUERY = """
            SELECT * FROM REVIEW_LIKES
            WHERE review_id = ?
            AND user_id = ?
            """;

    private static final String UPDATE_LIKE_QUERY = """
            UPDATE REVIEW_LIKES
            SET reaction = ?
            WHERE id = ?
            """;

    private static final String DELETE_LIKE_QUERY = """
            DELETE FROM REVIEW_LIKES
            WHERE review_id = ?
            AND user_id = ?
            """;

    public ReviewLikeRepository(JdbcTemplate jdbcTemplate, RowMapper<ReviewLike> reviewLikeRowMapper) {
        super(jdbcTemplate, reviewLikeRowMapper);
    }

    @Override
    public void addLikeToReview(Integer reviewId, Integer userId) {
        log.info("ReviewLikeRepository - Добавление лайка отзыву с id - {} от пользователя с id - {}", reviewId, userId);
        final Integer id = getReactionId(reviewId, userId);
        if (id != null) {
            update(UPDATE_LIKE_QUERY, positiveReaction, id);
        } else {
            insertToDatabase(ADD_LIKE_QUERY, positiveReaction, reviewId, userId);
        }
        log.info("ReviewRepository - Пользователь - {}, поставил лайк отзыву - {}", userId, reviewId);
    }

    @Override
    public void addDislikeToReview(Integer reviewId, Integer userId) {
        log.info("ReviewLikeRepository - Добавление дизлайка отзыву с id - {} от пользователя с id - {}", reviewId, userId);
        final Integer id = getReactionId(reviewId, userId);
        if (id != null) {
            update(UPDATE_LIKE_QUERY, negativeReaction, id);
        } else {
            insertToDatabase(ADD_LIKE_QUERY, negativeReaction, reviewId, userId);
        }
        log.info("ReviewRepository - Пользователь - {}, поставил дизлайк отзыву - {}", userId, reviewId);
    }

    @Override
    public void deleteLikeFromReview(Integer reviewId, Integer userId) {
        log.info("ReviewLikeRepository - Удаление лайка у отзыва с id - {} пользователем с id - {}", reviewId, userId);
        delete(DELETE_LIKE_QUERY, reviewId, userId);
        log.info("ReviewRepository - Пользователь - {}, удалил лайк у отзыва - {}", userId, reviewId);
    }

    @Override
    public void deleteDislikeFromReview(Integer reviewId, Integer userId) {
        log.info("ReviewLikeRepository - Удаление дизлайка у отзыва с id - {} пользователем с id - {}", reviewId, userId);
        delete(DELETE_LIKE_QUERY, reviewId, userId);
        log.info("ReviewRepository - Пользователь - {}, удалил дизлайк у отзыва - {}", userId, reviewId);
    }

    private Integer getReactionId(Integer reviewId, Integer userId) {
        ReviewLike reviewLike = getOne(GET_LIKE_ID_QUERY, reviewId, userId).orElse(null);
        return reviewLike != null ? reviewLike.getId() : null;
    }
}