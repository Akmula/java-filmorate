package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class FilmExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, Film> filmHashMap = new LinkedHashMap<>();
        Film film;
        while (resultSet.next()) {

            Integer filmKey = resultSet.getInt("film_id");
            log.info("filmKey={}", filmKey);

            film = filmHashMap.get(filmKey);

            if (film == null) {
                film = new Film();
                film.setId(filmKey);
                film.setName(resultSet.getString("film_name"));
                film.setName(resultSet.getString("film_name"));
                film.setDescription(resultSet.getString("film_description"));
                film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
                film.setDuration(resultSet.getInt("duration"));
                film.setMpa(MPA.builder()
                        .id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .description(resultSet.getString("mpa_description"))
                        .build());
                film.setGenres(new HashSet<>());
                filmHashMap.putIfAbsent(filmKey, film);
            }

            Integer genreKey = resultSet.getInt("genre_id");
            log.info("genreKey={}", genreKey);

            if (genreKey > 0) {
                Genre genre = Genre.builder()
                        .id(genreKey)
                        .name(resultSet.getString("genre_name"))
                        .build();
                film.getGenres().add(genre);
            }
        }
        return new ArrayList<>(filmHashMap.values());
    }
}