package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void initFilmController() {
        filmController = new FilmController();
    }

    @Test
    void filmControllerCanReturnFilmsList() {
        filmController.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void filmControllerCanAddNewFilm() {
        Film film = filmController.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertNotNull(film);
    }

    @Test
    void filmControllerCantAddFilmWithReleaseBefore28December1895() {
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(Film.builder().name("name")
                .description("description").releaseDate(LocalDate.of(1800, 10, 15)).duration(90)
                .build()));
    }

    @Test
    void filmControllerCantAddNewFilmIfFilmWithSameNameExist() {
        filmController.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        assertThrows(DuplicateException.class, () -> filmController.addFilm(Film.builder().name("name")
                .description("description1").releaseDate(LocalDate.of(2021, 10, 20))
                .duration(90).build()));
    }

    @Test
    void filmControllerCanUpdateInformationAbout() {
        filmController.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        Film film = filmController.updateFilm(updatedFilm);
        assertEquals(film, updatedFilm);
    }

    @Test
    void filmControllerThrowExceptionIfIdOfFilmToUpdateIsNull() {
        Film updatedFilm = Film.builder().name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(ConditionsNotMetException.class, () -> filmController.updateFilm(updatedFilm));
    }

    @Test
    void filmControllerThrowExceptionWhenTryUpdateFilmWithNotExistId() {
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(NotFoundException.class, () -> filmController.updateFilm(updatedFilm));
    }

    @Test
    void filmControllerNotChangeInformationAboutFilmIfAllFieldsIsNull() {
        Film film = filmController.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        Film updatedFilm = filmController.updateFilm(Film.builder().id(1).build());
        assertEquals(film, updatedFilm);
    }
}