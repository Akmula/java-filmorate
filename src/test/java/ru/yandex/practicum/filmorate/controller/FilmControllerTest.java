package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MPARepository;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;
    MPARepository mpaRepository;
    LikeRepository likeRepository;
    GenreRepository genreRepository;
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage, mpaRepository, genreRepository, likeRepository);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenTheNameIsIncorrectWeThrowAnValidationException() {
        FilmRequest filmRequest = FilmRequest.builder()
                .name("")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmRequest));
    }

    @Test
    public void whenTheDescriptionIsMoreThan200CharactersWeThrowAnValidationException() {
        FilmRequest filmRequest = FilmRequest.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.                                            ")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmRequest));
    }

    @Test
    public void whenReleaseDateIsEnteredIncorrectlyWeThrowAnValidationException() {
        FilmRequest filmRequest = FilmRequest.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.")
                .releaseDate(LocalDate.parse("1084-10-26"))
                .duration(108)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmRequest));
    }

    @Test
    public void whenTheDurationIsEqualToOrLessThan0WeThrowAnValidationException() {
        FilmRequest filmRequest = FilmRequest.builder()
                .name("Терминатор")
                .description("История противостояния солдата Кайла Риза и киборга-терминатора," +
                        " прибывших в 1984 год из пост-апокалиптического будущего, где миром правят машины-убийцы," +
                        " а человечество находится на грани вымирания.                                            ")
                .releaseDate(LocalDate.parse("1984-10-26"))
                .duration(0)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(filmRequest));
    }
}