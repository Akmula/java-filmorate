package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends RuntimeException {
    private static final String MSG_TEMPLATE = "Фильм с id = %d не найден!";

    public FilmNotFoundException(int id) {
        super(MSG_TEMPLATE.formatted(id));
    }
}