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
    @NotBlank(message = "не задано название фильма")
    private String name;
    @Size(max = 200, message = "превышен лимит количества символов = 200")
    private String description;
    @NotNull(message = "не задана дата релиза")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма не может быть отрицательной")
    private Integer duration;
}
