package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

public interface DirectorStorage {

    Director createDirector(Director director);

    Director updateDirector(Director director);

    Optional<Director> getDirectorById(Integer id);

    Collection<Director> getAllDirectors();

    void deleteDirector(Integer id);

    void deleteAllDirectors();
}
