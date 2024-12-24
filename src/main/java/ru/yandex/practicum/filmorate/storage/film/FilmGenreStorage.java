package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

public interface FilmGenreStorage {

    void addFilmGenre(Integer filmId, Integer genreId);

    void updateFilmGenres(Integer filmId, Integer genreId);

    Collection<FilmGenre> getGenresForFilm(Integer filmId);

    void deleteGenresForFilm(Integer filmId);
}