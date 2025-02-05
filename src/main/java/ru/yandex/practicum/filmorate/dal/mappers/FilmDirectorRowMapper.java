package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmDirectors;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDirectorRowMapper implements RowMapper<FilmDirectors> {

    @Override
    public FilmDirectors mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmDirectors.builder()
                .id(resultSet.getInt("id"))
                .filmId(resultSet.getInt("film_id"))
                .directorId(resultSet.getInt("director_id"))
                .build();
    }
}