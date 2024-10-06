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
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validationReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validationReleaseDate(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private void validationReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(INTERNATIONAL_FILM_DAY)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + INTERNATIONAL_FILM_DAY + "!");
        }
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}