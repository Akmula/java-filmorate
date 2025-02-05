package ru.yandex.practicum.filmorate.dal;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    protected Integer insertToDatabase(String query, Object... args) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < args.length; idx++) {
                ps.setObject(idx + 1, args[idx]);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKeyAs(Integer.class);
    }

    protected void update(String query, Object... params) {
        jdbcTemplate.update(query, params);
    }

    protected Optional<T> getOne(String query, Object... args) {
        try {
            T result = jdbcTemplate.queryForObject(query, rowMapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected Collection<T> getAll(String query, Object... args) {
        return jdbcTemplate.query(query, rowMapper, args);
    }

    protected List<Integer> getAsList(String query, Integer userId) {
        return jdbcTemplate.queryForList(query, new Object[]{userId}, Integer.class);
    }

    protected void delete(String query, Object... args) {
        jdbcTemplate.update(query, args);
    }

    protected void batchUpdate(List<Integer> ids, String query, Integer id) {
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(@NonNull PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }
}