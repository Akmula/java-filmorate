package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {

    private static final String GET_ALL_GENRES_QUERY = """
            SELECT * FROM GENRES
            ORDER BY genre_id ASC
            """;

    private static final String GET_GENRE_BY_ID_QUERY = """
            SELECT * FROM GENRES
            WHERE genre_id = ?
            """;

    private static final String GET_GENRES_BY_FILM_ID_QUERY = """
            SELECT g.*, fg.film_id AS film_id
            FROM GENRES AS g
            JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id
            WHERE film_id = ?
            """;

    public GenreRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> genreRowMapper) {
        super(jdbcTemplate, genreRowMapper);
    }

    public Collection<Genre> getAllGenres() {
        log.info("GenreRepository - Получение жанров из базы");
        return getAll(GET_ALL_GENRES_QUERY);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        log.info("GenreRepository - Получение жанра из базы по id - {}", genreId);
        return getOne(GET_GENRE_BY_ID_QUERY, genreId);
    }

    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        log.info("GenreRepository - Получение жанров из базы по id - {}", filmId);
        return getAll(GET_GENRES_BY_FILM_ID_QUERY, filmId);
    }
}