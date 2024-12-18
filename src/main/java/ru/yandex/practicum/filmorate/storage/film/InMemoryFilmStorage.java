package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmId = 0;

    @Override
    public Film createFilm(Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(++filmId);
            log.info("Фильму присвоен id - {}", film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(id);
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addLikeFilm(Integer userId, Integer filmId) {

    }

    @Override
    public void deleteLikeFilm(Integer userId, Integer filmId) {

    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        return List.of();
    }
}