package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {

    private static final int MAX_SIZE_CONTENT = 255;

    private Integer reviewId;

    @NotBlank(message = "Отзыв не может быть пустым!")
    @Size(max = MAX_SIZE_CONTENT, message = "Максимальная длина описания — " + MAX_SIZE_CONTENT + " символов!")
    private String content;

    @NotNull(message = "Тип отзыва должен быть указан!")
    private Boolean isPositive;

    private Integer userId;
    private Integer filmId;
    private Integer useful; // рейтинг полезности
}