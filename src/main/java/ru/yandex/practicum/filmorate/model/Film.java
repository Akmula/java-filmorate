package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private static final int MAX_SIZE_DESCRIPTION = 200;
    private Integer id;

    @NotBlank(message = "Название не может быть пустым!")
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = "Максимальная длина описания — " + MAX_SIZE_DESCRIPTION + " символов!")
    private String description;
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом!")
    private Integer duration;
    private MPA mpa;

    private Set<Genre> genres;
    private Set<Director> directors;

    @JsonIgnore
    private Set<Integer> likes;

    private Integer rate;
}