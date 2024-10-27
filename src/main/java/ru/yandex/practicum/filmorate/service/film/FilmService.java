package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private static final int MAX_SIZE_DESCRIPTION = 200;
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film saveFilm(Film film) {
        return filmStorage.saveFilm(validationFilm(film));
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(validationFilm(film));
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        validate(filmId, userId).addLike(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        validate(filmId, userId).deleteLike(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopular(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getRate).reversed())
                .limit(count).toList();
    }

    public Film validate(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return film;
    }

    private Film validationFilm(Film film) {
        log.info("Валидация фильма: {}", film);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            throw new ValidationException("Максимальная длина описания — " + MAX_SIZE_DESCRIPTION + " символов!");
        }
        if (film.getReleaseDate().isBefore(INTERNATIONAL_FILM_DAY)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + INTERNATIONAL_FILM_DAY + "!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getRate() == null) {
            film.setRate(0);
        }
        return film;
    }
}