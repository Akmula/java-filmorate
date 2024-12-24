package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

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

    private static final String GET_ALL_FILM_QUERY = """
            SELECT f.*, m.name AS mpa_name, m.description AS mpa_description
            FROM FILMS AS f
            JOIN MPA AS m ON f.mpa_id = m.mpa_id
            """;

    private static final String GET_FILM_BY_ID_QUERY = GET_ALL_FILM_QUERY + """
            WHERE f.film_id = ?
            """;

    private static final String GET_POPULAR_QUERY = GET_ALL_FILM_QUERY + """
            LEFT JOIN (
            SELECT l.film_id,
            COUNT(l.user_id) AS likes_count
            FROM LIKES l
            GROUP BY l.film_id
            ORDER BY likes_count DESC
            ) AS flc
            ON f.film_id = flc.film_id
            ORDER BY flc.likes_count DESC
            LIMIT ?
            """;

    public FilmRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> filmRowMapper,
                          LikeRepository likeRepository, FilmGenreRepository filmGenreRepository) {
        super(jdbcTemplate, filmRowMapper);
        this.likeRepository = likeRepository;
        this.filmGenreRepository = filmGenreRepository;
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
    public Optional<Film> getFilmById(Integer filmId) {
        log.info("FilmRepository - Получение фильма из базы по id - {}", filmId);
        Optional<Film> film = getOne(GET_FILM_BY_ID_QUERY, filmId);
        log.info("FilmRepository - Получен фильм - {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("FilmRepository - Получение фильмов из базы");
        return getAll(GET_ALL_FILM_QUERY);
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
    public Collection<Film> getPopularFilms(Integer count) {
        log.info("FilmRepository - Получение популярных фильмов из базы");
        return getAll(GET_POPULAR_QUERY, count);
    }

    private void setGenresForFilm(Film film) {
        if (film.getGenres() != null) {
            HashSet<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toCollection(HashSet::new));
            filmGenreRepository.batchUpdate(new ArrayList<>(genreIds), film.getId());
        }
    }
}