package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Director {
    private Integer id;
    @NotBlank(message = "не задано имя режиссера")
    private String name;
}
