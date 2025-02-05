package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class DirectorRepository extends BaseRepository<Director> implements DirectorStorage {

    private static final String CREATE_DIRECTOR_QUERY = """
            INSERT INTO DIRECTORS (name)
            VALUES (?)
            """;

    private static final String UPDATE_DIRECTOR_QUERY = """
            UPDATE DIRECTORS
            SET name = ?
            WHERE director_id = ?
            """;

    private static final String GET_ALL_DIRECTORS_QUERY = """
            SELECT director_id, name AS director_name FROM DIRECTORS
            ORDER BY director_id
            """;

    private static final String GET_DIRECTOR_BY_ID_QUERY = """
            SELECT director_id, name AS director_name FROM DIRECTORS
            WHERE director_id = ?
            """;

    private static final String DELETE_DIRECTOR_QUERY = """
            DELETE FROM DIRECTORS
            WHERE director_id = ?
            """;

    private static final String DELETE_ALL_DIRECTOR_QUERY = """
            DELETE FROM DIRECTORS
            WHERE director_id NOT IN (SELECT director_id FROM FILM_DIRECTORS)
            """;

    public DirectorRepository(JdbcTemplate jdbcTemplate, RowMapper<Director> directorRowMapper) {
        super(jdbcTemplate, directorRowMapper);
    }

    @Override
    public Director createDirector(Director director) {
        log.info("DirectorRepository - Добавление режиссера {} в базу", director);
        Integer id = insertToDatabase(
                CREATE_DIRECTOR_QUERY,
                director.getName()
        );
        director.setId(id);
        log.info("DirectorRepository - Режиссер {} добавлен в базу данных", director);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        log.info("DirectorRepository - Обновление режиссера {} в базе", director);
        update(
                UPDATE_DIRECTOR_QUERY,
                director.getName(),
                director.getId()
        );
        log.info("DirectorRepository - Режиссер {} обновлен", director);
        return director;
    }

    @Override
    public Optional<Director> getDirectorById(Integer directorId) {
        log.info("DirectorRepository - Получение режиссера из базы по id - {}", directorId);
        Optional<Director> director = getOne(GET_DIRECTOR_BY_ID_QUERY, directorId);
        log.info("DirectorRepository - Получен режиссер по id - {}", director);
        return director;
    }

    @Override
    public Collection<Director> getAllDirectors() {
        log.info("DirectorRepository - Получение режиссеров из базы");
        return getAll(GET_ALL_DIRECTORS_QUERY);
    }

    @Override
    public void deleteDirector(Integer directorId) {
        log.info("DirectorRepository - Удаление режиссера по id - {} из базы", directorId);
        update(DELETE_DIRECTOR_QUERY, directorId);
    }

    @Override
    public void deleteAllDirectors() {
        log.info("DirectorRepository - Удаление всех режиссеров из базы");
        update(DELETE_ALL_DIRECTOR_QUERY);
    }
}