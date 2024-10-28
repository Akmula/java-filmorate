package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;

    @NotEmpty(message = "Электронная почта не может быть пустой и должна содержать символ @!")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @!!")
    private String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    private String login;
    private String name;

    @Past(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friends;

    public void addFriend(Integer id) {
        this.friends.add(id);
    }

    public void deleteFriend(Integer id) {
        this.friends.remove(id);
    }
}