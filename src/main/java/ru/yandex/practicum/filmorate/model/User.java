package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class User {
    private Integer id;
    @NotNull(message = "email должен быть задан")
    @Email(message = "некорректный формат email")
    private String email;
    @NotBlank(message = "логин не может быть пустым")
    private String login;
    private String name;
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}
