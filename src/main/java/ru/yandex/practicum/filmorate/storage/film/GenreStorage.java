package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getGenreById(Integer genreId);

    Collection<Genre> getAllGenres();
}