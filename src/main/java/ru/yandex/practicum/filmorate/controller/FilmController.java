package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("POST /films - Запрос на добавление фильма: {}", film);
        Film response = filmService.saveFilm(film);
        log.info("POST /films - Ответ на добавление фильма: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        System.out.println("qwewerr");
        log.info("PUT /films - Запрос на обновление фильма: {}", film);
        Film response = filmService.updateFilm(film);
        log.info("PUT /films - Ответ на обновление фильма: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        log.info("GET /films - Запрос на получение фильмов");
        Collection<Film> response = filmService.getAllFilms();
        log.info("GET /films - Ответ на получение фильмов: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        log.info("GET /films - Запрос на получение фильма по id: {}", filmId);
        Film response = filmService.getFilmById(filmId);
        log.info("GET /films - Ответ на получение фильма по id: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT /films - Запрос на добавление лайка фильму id: {}, от пользователя: {}", filmId, userId);
        Film response = filmService.addLike(filmId, userId);
        log.info("PUT /films - Фильму - {}, поставлен лайк от пользователя с id - {}.", response, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("DELETE /films - Запрос на удаление лайка у фильма id: {}, от пользователя: {}", filmId, userId);
        Film response = filmService.deleteLike(filmId, userId);
        log.info("DELETE /films - У фильма - {}, удален лайк пользователем с id - {}.", response, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /films - Запрос на получение популярных фильмов. Выводить: {} фильмов", count);
        Collection<Film> response = filmService.getPopular(count);
        log.info("GET /films - Ответ на получение популярных фильмов: {}", response);
        return ResponseEntity.ok(response);
    }
}