package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody FilmRequest filmRequest) {
        log.info("POST /films - Запрос на добавление фильма: {}", filmRequest);
        FilmDto filmDto = filmService.createFilm(filmRequest);
        log.info("POST /films - Ответ на добавление фильма: {}", filmDto);
        return ResponseEntity.ok(filmDto);
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody FilmRequest filmRequest) {
        log.info("PUT /films - Запрос на обновление фильма: {}", filmRequest);
        FilmDto filmDto = filmService.updateFilm(filmRequest);
        log.info("PUT /films - Ответ на обновление фильма: {}", filmDto);
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping
    public ResponseEntity<Collection<FilmDto>> getFilms() {
        log.info("GET /films - Запрос на получение фильмов");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Integer filmId) {
        log.info("GET /films - Запрос на получение фильма по id: {}", filmId);
        FilmDto filmDto = filmService.getFilmById(filmId);
        log.info("GET /films - Ответ на получение фильма по id: {}", filmDto);
        return ResponseEntity.ok(filmService.getFilmById(filmId));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<FilmDto> addLikeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT /films - Запрос на добавление лайка фильму id: {}, от пользователя: {}", filmId, userId);
        return ResponseEntity.ok(filmService.addLikeFilm(filmId, userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<FilmDto> deleteLikeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("DELETE /films - Запрос на удаление лайка у фильма id: {}, от пользователя: {}", filmId, userId);
        return ResponseEntity.ok(filmService.deleteLikeFilm(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<FilmDto>> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /films - Запрос на получение популярных фильмов. Выводить: {} фильмов", count);
        Collection<FilmDto> response = filmService.getPopularFilms(count);
        log.info("GET /films - Ответ на получение популярных фильмов: {}", response);
        return ResponseEntity.ok(response);
    }
}