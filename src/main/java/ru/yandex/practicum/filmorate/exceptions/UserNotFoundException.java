package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    private static final String MSG_TEMPLATE = "Пользователь с id = %d не найден!";

    public UserNotFoundException(int id) {
        super(MSG_TEMPLATE.formatted(id));
        log.error(MSG_TEMPLATE.formatted(id));
    }
}