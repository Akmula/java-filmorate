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
        Film addedFilm = filmService.saveFilm(film);
        log.info("POST /films - Ответ на добавление фильма: {}", addedFilm);
        return ResponseEntity.ok(addedFilm);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT /films - Запрос на обновление фильма: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("PUT /films - Ответ на обновление фильма: {}", updatedFilm);
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        log.info("GET /films - Запрос на получение фильмов");
        Integer numberOfFilms = filmService.getAllFilms().size();
        log.info("GET /films - В ответ на получение фильмов, отправлено {} фильмов.", numberOfFilms);
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        log.info("GET /films - Запрос на получение фильма по id: {}", filmId);
        Film film = filmService.getFilmById(filmId);
        log.info("GET /films - Ответ на получение фильма по id: {}", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT /films - Запрос на добавление лайка фильму id: {}, от пользователя: {}", filmId, userId);
        Film film = filmService.addLike(filmId, userId);
        log.info("PUT /films - Фильму с id - {}, поставлен лайк от пользователя с id - {}.", filmId, userId);
        return ResponseEntity.ok(film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("DELETE /films - Запрос на удаление лайка у фильма id: {}, от пользователя: {}", filmId, userId);
        Film film = filmService.deleteLike(filmId, userId);
        log.info("DELETE /films - У фильма с id - {}, удален лайк пользователем с id - {}.", filmId, userId);
        return ResponseEntity.ok(film);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /films - Запрос на получение популярных фильмов. Выводить: {} фильмов", count);
        Integer numberOfFilms = filmService.getPopular(count).size();
        log.info("GET /films - Ответ на получение популярных фильмов. Вывод в кол-ве {} фильмов", numberOfFilms);
        return ResponseEntity.ok(filmService.getPopular(count));
    }
}