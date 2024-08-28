package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @NotNull(message = "email должен быть задан")
    @Email(message = "некорректный формат email")
    private String email;
    @NotNull(message = "не задан логин")
    @NotEmpty(message = "логин не может быть пустым")
    private String login;
    private String name;
    @NotNull(message = "не задана дата рождения")
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}
