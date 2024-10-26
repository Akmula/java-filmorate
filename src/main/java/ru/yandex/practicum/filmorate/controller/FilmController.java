package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("POST /films - Запрос на добавление фильма: {}", film);
        return ResponseEntity.ok(filmStorage.saveFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT /films - Запрос на обновление фильма: {}", film);
        return ResponseEntity.ok(filmStorage.updateFilm(film));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        log.info("GET /films - Запрос на получение фильмов");
        return ResponseEntity.ok(filmStorage.getAllFilms());
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        log.info("GET /films - Запрос на получение фильма по id: {}", filmId);
        return ResponseEntity.ok(filmStorage.getFilmById(filmId));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT /films - Запрос на добавление лайка фильму id: {}, от пользователя: {}", filmId, userId);
        return ResponseEntity.ok(filmService.addLike(filmId, userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("DELETE /films - Запрос на удаление лайка у фильма id: {}, от пользователя: {}", filmId, userId);
        return ResponseEntity.ok(filmService.deleteLike(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /films - Запрос на получение популярных фильмов. Выводить: {} фильмов", count);
        return ResponseEntity.ok(filmService.getPopular(count));
    }
}