package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public class GenreMapper {
    public static Genre mapToGenre(GenreDto genreDto) {
        return Genre.builder()
                .name(genreDto.getName())
                .build();
    }

    public static GenreDto mapToGenreDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static Collection<GenreDto> mapToGenreDtoList(Collection<Genre> genreList) {
        return genreList.stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }
}