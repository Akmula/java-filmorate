package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public class MPAMapper {
    public static MPA mapToMPA(MPADto mpaDto) {
        return MPA.builder()
                .name(mpaDto.getName())
                .description(mpaDto.getDescription())
                .build();
    }

    public static MPADto mapToMPADto(MPA mpa) {
        return MPADto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .description(mpa.getDescription())
                .build();
    }

    public static Collection<MPADto> mapToMPADtoList(Collection<MPA> mpaList) {
        return mpaList.stream()
                .map(MPAMapper::mapToMPADto)
                .toList();
    }
}