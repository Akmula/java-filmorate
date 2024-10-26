package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmNotFoundException extends RuntimeException {
    private static final String MSG_TEMPLATE = "Фильм с id = %d не найден!";

    public FilmNotFoundException(int id) {
        super(MSG_TEMPLATE.formatted(id));
        log.error(MSG_TEMPLATE.formatted(id));
    }
}