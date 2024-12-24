package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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

    private static final String GET_GENRES_FROM_FILM_QUERY = """
            SELECT fg.* FROM FILM_GENRE AS fg
            LEFT JOIN GENRES AS g
            ON fg.genre_id = g.genre_id
            WHERE film_id = ?
            """;

    private static final String DELETE_GENRES_FROM_FILM = """
            DELETE FROM FILM_GENRE
            WHERE film_id = ?
            """;

    public FilmGenreRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmGenre> filmGenreRowMapper) {
        super(jdbcTemplate, filmGenreRowMapper);
    }

    @Override
    public void addFilmGenre(Integer filmId, Integer genreId) {
        log.info("FilmGenreRepository - Добавление жанра с id - {}, фильму с id - {} в базу", genreId, filmId);
        insertToDatabase(ADD_FILM_GENRE_QUERY, filmId, genreId);
    }

    @Override
    public void updateFilmGenres(Integer filmId, Integer genreId) {
        log.info("FilmGenreRepository - Обновление жанра с id - {} у фильма с id - {}", genreId, filmId);
        update(UPDATE_FILM_GENRE_QUERY, filmId, genreId);
    }

    @Override
    public Collection<FilmGenre> getGenresForFilm(Integer filmId) {
        log.info("FilmGenreRepository - Получение жанров у фильма с id - {}", filmId);
        Collection<FilmGenre> filmGenres = getAll(GET_GENRES_FROM_FILM_QUERY, filmId);
        log.info("FilmGenreRepository - Получены жанры фильма - {}", filmGenres);
        return filmGenres;
    }

    @Override
    public void deleteGenresForFilm(Integer filmId) {
        log.info("FilmGenreRepository - Удаление жанров у фильма с id - {}", filmId);
        delete(DELETE_GENRES_FROM_FILM, filmId);
        log.info("FilmGenreRepository - Удалены жанры у фильма с id - {}", filmId);
    }

    protected void batchUpdate(List<Integer> genreIds, Integer filmId) {
        jdbcTemplate.batchUpdate(ADD_FILM_GENRE_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, filmId);
                preparedStatement.setInt(2, genreIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return genreIds.size();
            }
        });
    }
}