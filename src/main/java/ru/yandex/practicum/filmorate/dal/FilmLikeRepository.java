package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Collection;

@Slf4j
@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO LIKES (film_id, user_id)
            VALUES (?, ?)
            """;

    private static final String GET_ALL_LIKE_QUERY = """
            SELECT * FROM LIKES
            """;

    private static final String GET_BY_FILM_ID_QUERY = GET_ALL_LIKE_QUERY + " WHERE film_id = ?";

    private static final String DELETE_LIKE_QUERY = """
            DELETE FROM LIKES
            WHERE film_id = ?
            AND user_id = ?
            """;

    public FilmLikeRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmLike> likeRowMapper) {
        super(jdbcTemplate, likeRowMapper);
    }

    public void addLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmLikeRepository - Добавление лайка фильму с id -  {}", filmId);
        insertToDatabase(ADD_LIKE_QUERY, filmId, userId);
    }

    public Collection<FilmLike> getLikeFilm(Integer filmId) {
        log.info("FilmLikeRepository - Получение лайков у фильма с id -  {}", filmId);
        return getAll(GET_BY_FILM_ID_QUERY, filmId);
    }

    public void deleteLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmLikeRepository - Удаление лайка у фильма с id -  {}", filmId);
        delete(DELETE_LIKE_QUERY, filmId, userId);
    }
}