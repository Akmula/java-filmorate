package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmDirectors;

import java.util.Collection;

public interface FilmDirectorStorage {

    void addFilmDirector(Integer filmId, Integer directorId);

    void updateFilmDirectors(Integer filmId, Integer directorId);

    Collection<FilmDirectors> getDirectorForFilm(Integer filmId);

    void deleteDirectorsForFilm(Integer filmId);
}