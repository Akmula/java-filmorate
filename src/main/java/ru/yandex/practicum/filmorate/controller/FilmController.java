package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    protected Integer filmId = 0;
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Запрос POST /films - добавить фильм: {}", film);
        log.debug("Валидация даты релиза: {}", film.getReleaseDate());
        validationReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Ответ POST /films: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Запрос PUT /films - обновить фильм: {}", newFilm);
        log.debug("Проверка ID фильма {}", newFilm);
        if (newFilm.getId() == null) {
            log.error("Фильм {} не найден!", (Object) null);
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            log.debug("Проверка даты релиза {}", newFilm);
            validationReleaseDate(newFilm);
            films.put(newFilm.getId(), newFilm);
            log.info("Ответ PUT /films - обновленный фильм: {}", newFilm);
            return newFilm;
        }
        log.error("Ответ PUT /films - Фильм с id = {} не найден!", newFilm.getId());
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("GET /films - получение всех фильмов.");
        return films.values();
    }

    private void validationReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(INTERNATIONAL_FILM_DAY)) {
            log.error("Валидация даты релиза фильма {} не пройдена", film);
            throw new ValidationException("Дата релиза должна быть не раньше " + INTERNATIONAL_FILM_DAY + "!");
        }
    }

    private int getNextId() {
        return ++filmId;
    }
}