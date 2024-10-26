package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(Integer filmId, Integer userId) {
        validate(filmId, userId).addLike(userId);
        log.info("Фильму с id - {}, поставлен лайк от пользователя с id - {}.", filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        validate(filmId, userId).deleteLike(userId);
        log.info("У фильма с id - {}, удален лайк пользователем с id - {}.", filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopular(Integer count) {
        log.info("Ответ на запрос популярных фильмов. Вывод в кол-ве {} фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesSize).reversed())
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
}