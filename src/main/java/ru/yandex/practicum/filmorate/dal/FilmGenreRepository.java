package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;

import java.util.Optional;

@Slf4j
@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> implements FilmGenreStorage {

    private static final String ADD_FILM_GENRE_QUERY = """
            INSERT INTO
            FILM_GENRE (film_id, genre_id)
            VALUES(?, ?)
            """;

    private static final String UPDATE_FILM_GENRE_QUERY = """
            UPDATE FILM_GENRE
            SET genre_id = ?
            WHERE film_id = ?
            """;

    private static final String GET_GENRE_FROM_FILM_QUERY = """
            SELECT * FROM FILM_GENRE WHERE film_id = ? AND genre_id = ?
            """;

    public FilmGenreRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmGenre> filmGenreRowMapper) {
        super(jdbcTemplate, filmGenreRowMapper);
    }

    @Override
    public void addFilmGenre(Integer filmId, Integer genreId) {
        log.debug("FilmGenreRepository - Добавление жанра с id - {}, фильму с id - {} в базу", genreId, filmId);
        insertToDatabase(ADD_FILM_GENRE_QUERY, filmId, genreId);
    }

    @Override
    public void updateFilmGenres(Integer filmId, Integer genreId) {
        log.debug("FilmGenreRepository - Обновление жанра с id - {} у фильма с id - {}", genreId, filmId);
        update(UPDATE_FILM_GENRE_QUERY, filmId, genreId);
    }

    @Override
    public Optional<FilmGenre> getGenresForFilm(Integer filmId, Integer genreId) {
        log.debug("FilmGenreRepository - Получение жанров у фильма с id - {}", filmId);
        return getOne(GET_GENRE_FROM_FILM_QUERY, filmId, genreId);
    }
}