package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public FilmDto createFilm(@Valid @RequestBody FilmRequest filmRequest) {
        log.info("POST /films - Запрос на добавление фильма: {}", filmRequest);
        FilmDto filmDto = filmService.createFilm(filmRequest);
        log.info("POST /films - Ответ на добавление фильма: {}", filmDto);
        return filmDto;
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody FilmRequest filmRequest) {
        log.info("PUT /films - Запрос на обновление фильма: {}", filmRequest);
        FilmDto filmDto = filmService.updateFilm(filmRequest);
        log.info("PUT /films - Ответ на обновление фильма: {}", filmDto);
        return filmDto;
    }

    @DeleteMapping("/{filmId}")
    public FilmDto deleteFilm(@PathVariable int filmId) {
        log.info("DELETE /films - Запрос на удаление фильма по ID: {}", filmId);
        return filmService.deleteFilm(filmId);
    }

    @GetMapping
    public Collection<FilmDto> getFilms() {
        log.info("GET /films - Запрос на получение фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable int filmId) {
        log.info("GET /films - Запрос на получение фильма по id: {}", filmId);
        FilmDto filmDto = filmService.getFilmById(filmId);
        log.info("GET /films - Ответ на получение фильма по id: {}", filmDto);
        return filmDto;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto addLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.info("PUT /films - Запрос на добавление лайка фильму id: {}, от пользователя: {}", filmId, userId);
        return filmService.addLikeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto deleteLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.info("DELETE /films - Запрос на удаление лайка у фильма id: {}, от пользователя: {}", filmId, userId);
        return filmService.deleteLikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films - Запрос на получение популярных фильмов. Выводить: {} фильмов", count);
        Collection<FilmDto> response = filmService.getPopularFilms(count);
        log.info("GET /films - Ответ на получение популярных фильмов: {}", response);
        return response;
    }

    @GetMapping("/common")
    public Collection<FilmDto> getCommonFilms(@RequestParam(name = "userId") int userId,
                                              @RequestParam(name = "friendId") int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}