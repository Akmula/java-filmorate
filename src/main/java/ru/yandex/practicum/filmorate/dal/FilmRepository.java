package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final LikeRepository likeRepository;
    private final FilmGenreRepository filmGenreRepository;

    private static final String INSERT_FILM_QUERY = """
            INSERT INTO FILMS
            (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?)""";

    private static final String UPDATE_FILM_QUERY = """
            UPDATE FILMS
            SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?
            WHERE film_id = ?""";

    private static final String DELETE_FILM_QUERY = """
            DELETE FROM FILMS
            WHERE film_id = ?""";

    private static final String GET_ALL_FILM_QUERY = """
            SELECT f.film_id, f.name AS film_name, f.description AS film_description, f.release_date, f.duration,
                   m.mpa_id, m.name AS mpa_name, m.description AS mpa_description,
                   g.genre_id AS genre_id, g.NAME AS genre_name,
            COUNT (l.film_id) AS rate
            FROM FILMS AS f
            JOIN MPA AS m ON f.mpa_id = m.mpa_id
            LEFT JOIN FILM_GENRE FG on f.film_id = fg.film_id
            LEFT JOIN GENRES AS g ON fg.genre_id = g.genre_id
            LEFT JOIN LIKES AS l on f.film_id = l.film_id
            GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration,
                     m.mpa_id, m.name, m.description, g.genre_id, g.name
            """;

    private static final String GET_FILM_BY_ID_QUERY = """
            SELECT f.film_id, f.name AS film_name, f.description AS film_description, f.release_date, f.duration,
                   m.mpa_id, m.name AS mpa_name, m.description AS mpa_description,
                   g.genre_id AS genre_id, g.NAME AS genre_name,
            COUNT (l.film_id) AS rate
            FROM FILMS AS f
            JOIN MPA AS m ON f.mpa_id = m.mpa_id
            LEFT JOIN FILM_GENRE FG on f.film_id = fg.film_id
            LEFT JOIN GENRES AS g ON fg.genre_id = g.genre_id
            LEFT JOIN LIKES AS l on f.film_id = l.film_id
            WHERE f.film_id = ?
            GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration,
                     m.mpa_id, m.name, m.description, g.genre_id, g.name
            """;

    private static final String GET_COMMON_FILMS_QUERY = """
            SELECT f.film_id, f.name AS film_name, f.description AS film_description, f.release_date, f.duration,
                   m.mpa_id, m.name AS mpa_name, m.description AS mpa_description,
                   g.genre_id AS genre_id, g.NAME AS genre_name,
                   COUNT(likes.user_id) AS rate
            FROM FILMS AS f
            JOIN MPA AS m ON f.mpa_id = m.mpa_id
            LEFT JOIN FILM_GENRE AS fg ON f.film_id = fg.film_id
            LEFT JOIN GENRES AS g ON fg.genre_id = g.genre_id
            LEFT JOIN LIKES AS likes ON f.film_id = likes.film_id
            WHERE f.film_id IN (
            SELECT l1.film_id
            FROM LIKES AS l1
            JOIN LIKES AS l2
            ON l1.film_id = l2.film_id
            WHERE l1.user_id = ? AND l2.user_id = ?
            ORDER BY f.film_id
            )
            GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration,
                     m.mpa_id, m.name, m.description, g.genre_id, g.name
            """;

    public FilmRepository(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper,


                          LikeRepository likeRepository, FilmGenreRepository filmGenreRepository) {
        super(jdbcTemplate, filmRowMapper);
        this.likeRepository = likeRepository;
        this.filmGenreRepository = filmGenreRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("FilmRepository - Добавление фильма {} в базу", film);
        Integer id = insertToDatabase(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        setGenresForFilm(film);
        log.info("FilmRepository - Фильм {} добавлен в базу данных", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("FilmRepository - Обновление фильма {} в базе", film);
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        setGenresForFilm(film);
        log.info("FilmRepository - Фильм {} обновлен в базе данных", film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        log.info("FilmRepository - Удаление фильма из базы - {}", film);
        delete(DELETE_FILM_QUERY, film.getId());
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        log.info("FilmRepository - Получение фильма из базы по id - {}", filmId);
        Optional<Film> film = Objects.requireNonNull(jdbcTemplate
                        .query(GET_FILM_BY_ID_QUERY, new FilmExtractor(), filmId))
                .stream().findFirst();
        log.info("FilmRepository - Получен фильм - {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("FilmRepository - Получение фильмов из базы");
        return jdbcTemplate.query(GET_ALL_FILM_QUERY, new FilmExtractor());
    }

    @Override
    public void addLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmRepository - Добавление лайка фильму");
        likeRepository.addLikeFilm(filmId, userId);
    }

    @Override
    public void deleteLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmRepository - Удаление лайка у фильма");
        likeRepository.deleteLikeFilm(filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("count", count);

        if (genreId != null) {
            params.put("genreId", genreId);
        }
        if (year != null) {
            params.put("year", year);
        }

        String sqlQuery = getSqlQuery(params);
        System.out.println(sqlQuery);
        log.info("FilmRepository - Получение популярных фильмов из базы");
        return namedParameterJdbcTemplate.query(sqlQuery, params, new FilmExtractor());
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        log.info("FilmRepository - Получение общих фильмов пользователей {} и {}.", userId, friendId);
        return jdbcTemplate.query(GET_COMMON_FILMS_QUERY, new FilmExtractor(), userId, friendId);
    }

    private void setGenresForFilm(Film film) {
        if (film.getGenres() != null) {
            HashSet<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toCollection(HashSet::new));
            filmGenreRepository.batchUpdate(new ArrayList<>(genreIds), film.getId());
        }
    }

    private String getSqlQuery(Map<String, Object> params) {

        String sqlQuery = """
                SELECT f.film_id, f.name AS film_name, f.description AS film_description, f.release_date, f.duration,
                       m.mpa_id, m.name AS mpa_name, m.description AS mpa_description,
                       g.genre_id AS genre_id, g.NAME AS genre_name,
                COUNT (l.film_id) AS rate
                FROM FILMS AS f
                JOIN MPA AS m ON f.mpa_id = m.mpa_id
                LEFT JOIN FILM_GENRE FG on f.film_id = fg.film_id
                LEFT JOIN GENRES AS g ON fg.genre_id = g.genre_id
                LEFT JOIN LIKES AS l on f.film_id = l.film_id
                """;

        String endSqlQuery = """
                GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration,
                          m.mpa_id, m.name, m.description, g.genre_id, g.name
                ORDER BY rate DESC
                LIMIT :count
                """;

        String sqlWhereYear = """
                WHERE EXTRACT(YEAR FROM f.release_date) = :year
                """;

        String sqlWhereGenre = """
                f.film_id IN (
                        SELECT film_id
                        FROM FILM_GENRE
                        WHERE genre_id = :genreId
                )
                """;

        if (params.get("year") != null && params.get("genreId") != null) {
            sqlQuery = sqlQuery + sqlWhereYear + " AND " + sqlWhereGenre;
        }

        if (params.get("year") != null && params.get("genreId") == null) {
            sqlQuery = sqlQuery + sqlWhereYear;
        }

        if (params.get("genreId") != null && params.get("year") == null) {
            sqlQuery = sqlQuery + " WHERE " + sqlWhereGenre;
        }

        return sqlQuery + endSqlQuery;
    }
}