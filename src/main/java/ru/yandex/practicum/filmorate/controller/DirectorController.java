package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.film.DirectorService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public DirectorDto createDirector(@Valid @RequestBody final DirectorDto directorDto) {
        log.info("POST/ directors - запрос на добавление режиссера: {}", directorDto);
        DirectorDto createdDirectorDto = directorService.createDirector(directorDto);
        log.info("POST/ directors - Ответ на добавление режиссера: {}", createdDirectorDto);
        return createdDirectorDto;
    }

    @PutMapping
    public DirectorDto updateDirector(@Valid @RequestBody final DirectorDto directorDto) {
        log.info("PUT/ directors - запрос на обновление режиссера: {}", directorDto);
        DirectorDto updatedDirectorDto = directorService.updateDirector(directorDto);
        log.info("PUT/ directors - Ответ на обновление режиссера: {}", updatedDirectorDto);
        return updatedDirectorDto;
    }

    @GetMapping
    public Collection<DirectorDto> getAllDirectors() {
        log.info("GET /directors - Запрос на получение режиссеров");
        return directorService.getAllDirectors();
    }

    @GetMapping("/{directorId}")
    public DirectorDto getDirectorById(@PathVariable int directorId) {
        log.info("GET /directors - Запрос на получение режиссера по id: {}", directorId);
        DirectorDto directorDto = directorService.getDirectorById(directorId);
        log.info("GET /directors - Ответ на получение режиссера по id: {}", directorDto);
        return directorDto;
    }

    @DeleteMapping("/{directorId}")
    public DirectorDto deleteDirector(@PathVariable Integer directorId) {
        log.info("DELETE /directors - Запрос на удаление режиссера по id: {}", directorId);
        return directorService.deleteDirector(directorId);
    }
}