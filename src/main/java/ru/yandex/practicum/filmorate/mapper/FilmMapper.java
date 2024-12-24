package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public final class FilmMapper {

    public static Film mapToFilm(FilmRequest filmRequest) {
        return Film.builder()
                .name(filmRequest.getName())
                .description(filmRequest.getDescription())
                .releaseDate(filmRequest.getReleaseDate())
                .duration(filmRequest.getDuration())
                .mpa(filmRequest.getMpa())
                .genres(filmRequest.getGenres())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .genres(film.getGenres())
                .likes(film.getLikes())
                .rate(film.getRate())
                .build();
    }

    public static Film updateFilmFields(Film updatedFilm, FilmRequest request) {
        updatedFilm.setId(request.getId());
        updatedFilm.setName(request.getName());
        updatedFilm.setDescription(request.getDescription());
        updatedFilm.setReleaseDate(request.getReleaseDate());
        updatedFilm.setDuration(request.getDuration());
        updatedFilm.setMpa(request.getMpa());
        updatedFilm.setGenres(request.getGenres());
        return updatedFilm;
    }
}