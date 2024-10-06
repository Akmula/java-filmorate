package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private static final int MAX_SIZE_DESCRIPTION = 200;

    private Integer id;

    @NotEmpty(message = "Название не может быть пустым!")
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = "Максимальная длина описания — " + MAX_SIZE_DESCRIPTION + " символов!")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом!")
    private Integer duration;
}