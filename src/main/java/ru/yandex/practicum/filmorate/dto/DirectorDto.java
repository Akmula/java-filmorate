package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectorDto {

    private Integer id;

    @NotBlank(message = "Имя не может быть пустым!")
    private String name;
}