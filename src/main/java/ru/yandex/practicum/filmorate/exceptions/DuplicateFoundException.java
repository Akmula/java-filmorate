package ru.yandex.practicum.filmorate.exceptions;

public class DuplicateFoundException extends RuntimeException {
    private static final String MSG_TEMPLATE = "%s уже существует в базе!";

    public DuplicateFoundException(String message) {
        super(MSG_TEMPLATE.formatted(message));
    }
}