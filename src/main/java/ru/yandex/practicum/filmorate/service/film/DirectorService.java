package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.util.Collection;

@Slf4j
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public DirectorService(@Qualifier("directorRepository") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public DirectorDto createDirector(DirectorDto directorDto) {
        log.info("DirectorService - Создание режиссера - {}", directorDto);
        Director director = DirectorMapper.mapToDirector(directorDto);
        DirectorDto createdDirectorDto = DirectorMapper.mapToDirectorDto(directorStorage.createDirector(director));
        log.info("DirectorService - Добавлен режиссер - {}", createdDirectorDto);
        return createdDirectorDto;
    }

    public DirectorDto updateDirector(DirectorDto directorDto) {
        log.info("DirectorService - Обновление режиссера - {}", directorDto);

        directorStorage.getDirectorById(directorDto.getId())
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + directorDto.getId() + " не найден!"));

        Director director = DirectorMapper.mapToDirector(directorDto);
        DirectorDto updatedDirectorDto = DirectorMapper.mapToDirectorDto(directorStorage.updateDirector(director));
        log.info("DirectorService - Обновленный режиссер - {}", updatedDirectorDto);
        return updatedDirectorDto;
    }

    public Collection<DirectorDto> getAllDirectors() {
        log.info("DirectorService - Получение всех режиссеров");
        Collection<DirectorDto> directorsDto = directorStorage.getAllDirectors()
                .stream().map(DirectorMapper::mapToDirectorDto).toList();
        log.info("DirectorService - Список всех режиссеров получен");
        return directorsDto;
    }

    public DirectorDto getDirectorById(Integer directorId) {
        log.info("DirectorService - Получения режиссера по id - {}", directorId);
        DirectorDto directorDto = directorStorage.getDirectorById(directorId)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + directorId + " не найден!"));
        log.info("DirectorService - Получен режиссер по id - {}", directorDto);
        return directorDto;
    }

    public DirectorDto deleteDirector(Integer directorId) {
        log.info("DirectorService - Удаление режиссера");
        Director deletedDirector = directorStorage.getDirectorById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + directorId + " не найден!"));
        directorStorage.deleteDirector(directorId);
        DirectorDto directorDto = DirectorMapper.mapToDirectorDto(deletedDirector);
        log.info("DirectorService - Удаленный режиссер: {}", directorDto);
        return directorDto;
    }
}