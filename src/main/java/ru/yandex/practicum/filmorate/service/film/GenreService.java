package ru.yandex.practicum.filmorate.service.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public GenreDto getGenreById(Integer genreId) {
        log.info("GenreService - Получение жанра по id - {}", genreId);
        GenreDto genreDto = genreStorage.getGenreById(genreId)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр с id - " + genreId + " не найден"));
        log.info("GenreService - Отправлен жанр - {}", genreDto);
        return genreDto;
    }

    public Collection<GenreDto> getAllGenres() {
        log.info("GenreService - Получение всех жанров");
        Collection<GenreDto> genreSDto = GenreMapper.mapToGenreDtoList(genreStorage.getAllGenres());
        log.info("GenreService - Получен список всех жанров - {}", genreSDto);
        return genreSDto;
    }
}