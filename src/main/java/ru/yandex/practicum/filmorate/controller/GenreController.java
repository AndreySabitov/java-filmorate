package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        return filmService.getGenreById(id);
    }
}
