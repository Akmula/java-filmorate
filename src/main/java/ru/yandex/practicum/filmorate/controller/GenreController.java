package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getAllGenres() {
        log.info("GET /genres - Запрос на получение жанров фильма");
        Collection<GenreDto> genresDto = genreService.getAllGenres();
        log.info("GET /genres - Ответ на получение жанров фильма: {}", genresDto);
        return genresDto;
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable int id) {
        log.info("GET /genres - Запрос на получение жанра фильма по id - {}", id);
        GenreDto genreDto = genreService.getGenreById(id);
        log.info("GET /genres - Ответ на получение жанра фильма по id - {}", genreDto);
        return genreDto;
    }
}