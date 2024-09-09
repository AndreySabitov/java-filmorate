package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmStorage.getFilmById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id,@PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }
}
