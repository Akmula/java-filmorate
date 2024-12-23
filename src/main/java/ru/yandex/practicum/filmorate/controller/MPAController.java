package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.service.film.MPAService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MPAController {

    private final MPAService mpaService;

    @GetMapping
    public Collection<MPADto> getAllMPAs() {
        log.info("GET /mpa - Запрос на получение категорий");
        Collection<MPADto> mpaDto = mpaService.getAllMPAs();
        log.info("GET /mpa - Ответ на получение категорий: {}", mpaDto);
        return mpaDto;
    }

    @GetMapping("/{id}")
    public MPADto getMPAById(@PathVariable int id) {
        log.info("GET /mpa - Запрос на получение категорий фильма по id - {}", id);
        MPADto mpaDto = mpaService.getMPAById(id);
        log.info("GET /mpa - Ответ на получение категорий фильма по id - {}", mpaDto);
        return mpaDto;
    }
}