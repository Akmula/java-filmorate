package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final int MAX_SIZE_DESCRIPTION = 200;
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);
    private final IdGenerator idGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film saveFilm(Film film) {
        validationFilm(film);
        films.put(film.getId(), film);
        log.info("Ответ на запрос добавления фильма - {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        validationFilm(film);
        films.put(film.getId(), film);
        log.info("Ответ на запрос обновления фильма - {}", film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(id);
        }
        log.info("Ответ на запрос получения фильма по id - {}", films.get(id));
        return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Ответ на запрос получения всех фильмов. Кол-во - {}", films.values().size());
        return films.values();
    }

    private void validationFilm(Film film) {
        log.info("Валидация фильма: {}", film);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            throw new ValidationException("Максимальная длина описания — " + MAX_SIZE_DESCRIPTION + " символов!");
        }
        if (film.getReleaseDate().isBefore(INTERNATIONAL_FILM_DAY)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + INTERNATIONAL_FILM_DAY + "!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(idGenerator.getNextFilmId());
            log.info("Фильму присвоен id - {}", film.getId());
        }
    }
}