package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    IdGenerator idGeneratorFilm;
    FilmStorage filmStorage;
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        idGeneratorFilm = new IdGenerator();
        filmStorage = new InMemoryFilmStorage(idGeneratorFilm);
        filmController = new FilmController();
    }

    @Test
    public void whenTheNameIsIncorrectWeThrowAnValidationException() {
        Film film = Film.builder()
                .name("")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmStorage.saveFilm(film)));
    }

    @Test
    public void whenTheDescriptionIsMoreThan200CharactersWeThrowAnValidationException() {
        Film film = Film.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.                                            ")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmStorage.saveFilm(film)));
    }

    @Test
    public void whenReleaseDateIsEnteredIncorrectlyWeThrowAnValidationException() {
        Film film = Film.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.")
                .releaseDate(LocalDate.parse("1084-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmStorage.saveFilm(film)));
    }

    @Test
    public void whenTheDurationIsEqualToOrLessThan0WeThrowAnValidationException() {
        Film film = Film.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.                                            ")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(0)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmStorage.saveFilm(film)));
    }
}