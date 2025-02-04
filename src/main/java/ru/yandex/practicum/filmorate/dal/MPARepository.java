package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.MPAStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class MPARepository extends BaseRepository<MPA> implements MPAStorage {
    private static final String GET_ALL_MPA_QUERY = "SELECT * FROM MPA";
    private static final String GET_MPA_BY_ID_QUERY = GET_ALL_MPA_QUERY + " WHERE mpa_id = ?";

    public MPARepository(JdbcTemplate jdbcTemplate, RowMapper<MPA> mpaRowMapper) {
        super(jdbcTemplate, mpaRowMapper);
    }

    @Override
    public Optional<MPA> getMpaById(Integer mpaId) {
        log.info("MPARepository - Получение MPA из базы по id - {}", mpaId);
        return getOne(GET_MPA_BY_ID_QUERY, mpaId);
    }

    @Override
    public Collection<MPA> getAllMPAs() {
        log.info("MPARepository - Получение всех MPA из базы");
        return getAll(GET_ALL_MPA_QUERY);
    }
}