package ru.yandex.practicum.filmorate.service.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.storage.film.MPAStorage;

import java.util.Collection;

@Slf4j
@Service
public class MPAService {
    private final MPAStorage mpaStorage;

    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MPADto getMPAById(Integer mpaId) {
        log.info("MPAService - Получение MPA по id - {}", mpaId);
        MPADto mpaDto = mpaStorage.getMpaById(mpaId)
                .map(MPAMapper::mapToMPADto)
                .orElseThrow(() -> new NotFoundException("MPA с id - " + mpaId + " не найден"));
        log.info("MPAService - Получен MPA - {}", mpaDto);
        return mpaDto;
    }

    public Collection<MPADto> getAllMPAs() {
        log.info("MPAService - Получение всех жанров");
        Collection<MPADto> mpaDto = MPAMapper.mapToMPADtoList(mpaStorage.getAllMPAs());
        log.info("MPAService - Получен список всех жанров - {}", mpaDto);
        return mpaDto;
    }
}