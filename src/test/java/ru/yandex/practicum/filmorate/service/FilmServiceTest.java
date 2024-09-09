package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    FilmStorage filmStorage;

    @BeforeEach
    void initFilmController() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void filmControllerCanReturnFilmsList() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void filmControllerCanAddNewFilm() {
        Film film = filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertNotNull(film);
    }

    @Test
    void filmControllerCantAddFilmWithReleaseBefore28December1895() {
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(Film.builder().name("name")
                .description("description").releaseDate(LocalDate.of(1800, 10, 15)).duration(90)
                .build()));
    }

    @Test
    void filmControllerCantAddNewFilmIfFilmWithSameNameExist() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        assertThrows(DuplicateException.class, () -> filmStorage.addFilm(Film.builder().name("name")
                .description("description1").releaseDate(LocalDate.of(2021, 10, 20))
                .duration(90).build()));
    }

    @Test
    void filmControllerCanUpdateInformationAboutFilm() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        Film film = filmStorage.updateFilm(updatedFilm);
        assertEquals(film, updatedFilm);
    }

    @Test
    void filmControllerThrowExceptionIfIdOfFilmToUpdateIsNull() {
        Film updatedFilm = Film.builder().name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(ValidationException.class, () -> filmStorage.updateFilm(updatedFilm));
    }

    @Test
    void filmControllerThrowExceptionWhenTryUpdateFilmWithNotExistId() {
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(updatedFilm));
    }
}