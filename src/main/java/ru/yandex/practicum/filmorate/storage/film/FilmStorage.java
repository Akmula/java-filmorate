package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    Optional<Film> getFilmById(Integer filmId);

    Collection<Film> getAllFilms();

    void addLikeFilm(Integer filmId, Integer userId);

    void deleteLikeFilm(Integer filmId, Integer userId);

    Collection<Film> getPopularFilms(Integer count, Integer genreId, Integer year);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Collection<Film> getDirectorFilms(Integer directorId, String sortBy);
}