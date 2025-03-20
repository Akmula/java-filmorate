package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserRequest {

    private Integer id;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    private String login;

    @NotEmpty(message = "Электронная почта не может быть пустой и должна содержать символ @!")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @!")
    private String email;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем!")
    @NotNull(message = "Дата рождения не может быть null!")
    private LocalDate birthday;
}