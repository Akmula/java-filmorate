package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

public interface MPAStorage {
    Optional<MPA> getMpaById(Integer mpaId);

    Collection<MPA> getAllMPAs();
}