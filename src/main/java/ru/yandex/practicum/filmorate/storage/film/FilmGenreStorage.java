package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Optional;

public interface FilmGenreStorage {

    void addFilmGenre(Integer filmId, Integer genreId);

    void updateFilmGenres(Integer filmId, Integer genreId);

    Optional<FilmGenre> getGenresForFilm(Integer filmId, Integer genreId);
}