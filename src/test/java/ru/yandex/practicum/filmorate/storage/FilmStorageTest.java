package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmStorageTest {
    FilmStorage filmStorage;

    @BeforeEach
    void initFilmStorage() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void filmStorageCanReturnFilmsList() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void filmStorageCanAddNewFilm() {
        Film film = filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90).build());
        assertNotNull(film);
    }

    @Test
    void filmStorageCantAddFilmWithReleaseBefore28December1895() {
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(Film.builder().name("name")
                .description("description").releaseDate(LocalDate.of(1800, 10, 15)).duration(90)
                .build()));
    }

    @Test
    void filmStorageCantAddNewFilmIfFilmWithSameNameExist() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        assertThrows(DuplicateException.class, () -> filmStorage.addFilm(Film.builder().name("name")
                .description("description1").releaseDate(LocalDate.of(2021, 10, 20))
                .duration(90).build()));
    }

    @Test
    void filmStorageCanUpdateInformationAboutFilm() {
        filmStorage.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90).build());
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        Film film = filmStorage.updateFilm(updatedFilm);
        assertEquals(film, updatedFilm);
    }

    @Test
    void filmStorageThrowExceptionIfIdOfFilmToUpdateIsNull() {
        Film updatedFilm = Film.builder().name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(ValidationException.class, () -> filmStorage.updateFilm(updatedFilm));
    }

    @Test
    void filmStorageThrowExceptionWhenTryUpdateFilmWithNotExistId() {
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90).build();
        assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(updatedFilm));
    }
}