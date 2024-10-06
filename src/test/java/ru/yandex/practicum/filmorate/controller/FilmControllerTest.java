package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void whenReleaseDateIsEnteredIncorrectlyWeThrowAnValidationException() {
        Film film = Film.builder()
                .releaseDate(LocalDate.parse("1002-12-12"))
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        System.out.println(filmController.getFilms());
    }
}