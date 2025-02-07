package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class ReviewRepository extends BaseRepository<Review> implements ReviewStorage {

    private static final String CREATE_REVIEW_QUERY = """
            INSERT INTO REVIEWS
            (content, positive, user_id, film_id)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_REVIEW_QUERY = """
            UPDATE REVIEWS
            SET content = ?, positive = ?, user_id = ?, film_id = ?
            WHERE review_id = ?
            """;

    private static final String GET_REVIEW_BY_ID_QUERY = """
            SELECT r.*, SUM(rl.REACTION) AS useful FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES AS rl ON r.review_id = rl.review_id
            WHERE r.review_id = ?
            GROUP BY r.REVIEW_ID, CONTENT, POSITIVE, r.USER_ID, FILM_ID
            ORDER BY useful DESC
            """;

    private static final String GET_REVIEW_BY_FILM_ID_QUERY = """
            SELECT r.*, SUM(rl.REACTION) AS useful FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES AS rl ON r.review_id = rl.review_id
            WHERE r.film_id = ?
            GROUP BY r.REVIEW_ID, CONTENT, POSITIVE, r.USER_ID, FILM_ID
            ORDER BY useful DESC
            LIMIT ?
            """;

    private static final String GET_ALL_REVIEW_QUERY = """
            SELECT r.*, SUM(rl.REACTION) AS useful FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES AS rl ON r.review_id = rl.review_id
            GROUP BY r.REVIEW_ID, CONTENT, POSITIVE, r.USER_ID, FILM_ID
            ORDER BY useful DESC
            LIMIT ?
            """;

    private static final String DELETE_REVIEW_QUERY = """
            DELETE FROM REVIEWS
            WHERE review_id = ?""";

    public ReviewRepository(JdbcTemplate jdbcTemplate, RowMapper<Review> reviewRowMapper) {
        super(jdbcTemplate, reviewRowMapper);
    }

    @Override
    public Review addReview(Review review) {
        log.info("ReviewRepository - Добавление отзыва {} в базу", review);

        Integer id = insertToDatabase(
                CREATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        review.setReviewId(id);
        log.info("ReviewRepository - Отзыв {} добавлен в базу данных", review);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        log.info("ReviewRepository - Обновление отзыва {} в базе", review);
        update(
                UPDATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId()
        );
        log.info("ReviewRepository - Отзыв {} обновлен", review);
        return review;
    }

    @Override
    public Review deleteReview(Integer reviewId) {
        log.info("ReviewRepository - Удаление отзыва из базы - {}", reviewId);
        Optional<Review> deletedReview = getReviewById(reviewId);
        delete(DELETE_REVIEW_QUERY, reviewId);
        return deletedReview.orElse(null);
    }

    @Override
    public Optional<Review> getReviewById(Integer reviewId) {
        log.info("ReviewRepository - Получение отзыва из базы по id - {}", reviewId);
        Optional<Review> review = getOne(GET_REVIEW_BY_ID_QUERY, reviewId);
        log.info("ReviewRepository - Получен отзыв по id - {}", review);
        return review;
    }

    @Override
    public Collection<Review> getReviewsByFilmId(Integer filmId, Integer count) {
        Collection<Review> reviews;
        if (filmId == null) {
            log.info("ReviewRepository - Получение отзывов из базы в кол-ве - {}", count);
            reviews = getAll(GET_ALL_REVIEW_QUERY, count);
            log.info("ReviewRepository - Получены отзывы - {}", reviews);

        } else {
            log.info("ReviewRepository - Получение отзыва из базы по id фильма - {}", filmId);
            reviews = getAll(GET_REVIEW_BY_FILM_ID_QUERY, filmId, count);
            log.info("ReviewRepository - Получены отзывы по id фильма - {}", reviews);
        }
        return reviews;
    }
}