package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotNull(message = "не задано название фильма")
    @NotBlank(message = "задано пустое название фильма")
    private String name;
    @Size(max = 200, message = "превышен лимит количества символов = 200")
    private String description;
    @NotNull(message = "не задана дата релиза")
    private LocalDate releaseDate;
    @NotNull(message = "не задана продолжительность фильма")
    @Positive(message = "продолжительность фильма не может быть отрицательной")
    private Integer duration;
}
