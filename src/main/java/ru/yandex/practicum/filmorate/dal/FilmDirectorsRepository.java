package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDirectors;
import ru.yandex.practicum.filmorate.storage.film.FilmDirectorStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDirectorsRepository extends BaseRepository<FilmDirectors> implements FilmDirectorStorage {

    private static final String ADD_FILM_DIRECTOR_QUERY = """
            INSERT INTO
            FILM_DIRECTORS (film_id, director_id)
            VALUES(?, ?)
            """;

    private static final String UPDATE_FILM_DIRECTOR_QUERY = """
            UPDATE FILM_DIRECTORS
            SET director_id = ?
            WHERE film_id = ?
            """;

    private static final String GET_DIRECTORS_FROM_FILM_QUERY = """
            SELECT fd.* FROM FILM_DIRECTORS AS fd
            LEFT JOIN DIRECTORS AS d
            ON fd.director_id = d.director_id
            WHERE film_id = ?
            """;

    private static final String DELETE_DIRECTOR_FROM_FILM = """
            DELETE FROM FILM_DIRECTORS
            WHERE film_id = ?
            """;

    public FilmDirectorsRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmDirectors> filmDirectorsRowMapper) {
        super(jdbcTemplate, filmDirectorsRowMapper);
    }

    @Override
    public void addFilmDirector(Integer filmId, Integer directorId) {
        log.info("FilmDirectorsRepository - Добавление режиссера с id - {}, фильму с id - {} в базу", directorId, filmId);
        insertToDatabase(ADD_FILM_DIRECTOR_QUERY, filmId, directorId);
    }

    @Override
    public void updateFilmDirectors(Integer filmId, Integer directorId) {
        log.info("FilmDirectorsRepository - Обновление режиссера с id - {} у фильма с id - {}", directorId, filmId);
        update(UPDATE_FILM_DIRECTOR_QUERY, filmId, directorId);
    }

    @Override
    public Collection<FilmDirectors> getDirectorForFilm(Integer filmId) {
        log.info("FilmDirectorsRepository - Получение режиссеров у фильма с id - {}", filmId);
        Collection<FilmDirectors> filmDirectors = getAll(GET_DIRECTORS_FROM_FILM_QUERY, filmId);
        log.info("FilmDirectorsRepository - Получены режиссеры фильма - {}", filmDirectors);
        return filmDirectors;
    }

    @Override
    public void deleteDirectorsForFilm(Integer filmId) {
        log.info("FilmDirectorsRepository - Удаление режиссеров у фильма с id - {}", filmId);
        delete(DELETE_DIRECTOR_FROM_FILM, filmId);
        log.info("FilmDirectorsRepository - Удалены режиссеры у фильма с id - {}", filmId);
    }

    protected void setDirectorsForFilm(Film film) {
        if (film.getDirectors() != null) {
            HashSet<Integer> directorIds = film.getDirectors().stream()
                    .map(Director::getId)
                    .collect(Collectors.toCollection(HashSet::new));
            batchUpdate(new ArrayList<>(directorIds), ADD_FILM_DIRECTOR_QUERY, film.getId());
        }
    }
}