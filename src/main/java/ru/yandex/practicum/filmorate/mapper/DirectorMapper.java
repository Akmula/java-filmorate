package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

@Service
public class DirectorMapper {

    public static Director mapToDirector(DirectorDto directorDto) {
        return Director.builder()
                .id(directorDto.getId())
                .name(directorDto.getName())
                .build();
    }

    public static DirectorDto mapToDirectorDto(Director director) {
        return DirectorDto.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }
}