package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.filmLikes.LikeStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmServiceTest {
    private final FilmService filmService;
    private final UserService userService;
    private final LikeStorage likeStorage;

    @Test
    void testCanAddNewFilm() {
        Film film = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        assertNotNull(film);
    }

    @Test
    void testCanReturnFilmsList() {
        Film film = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 15)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        assertEquals(1, filmService.getFilms().size());
    }

    @Test
    void testCantAddFilmWithReleaseBefore28December1895() {
        assertThrows(ValidationException.class, () -> filmService.addFilm(Film.builder().name("name")
                .description("description").releaseDate(LocalDate.of(1800, 10, 15)).duration(90)
                .mpa(MPA.builder().id(1).build()).build()));
    }

    @Test
    void testCanUpdateInformationAboutFilm() {
        Film film1 = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        Film updatedFilm = Film.builder().id(1).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).name("G").build()).build();
        Film film = filmService.updateFilm(updatedFilm);
        assertEquals(film.getName(), updatedFilm.getName());
    }

    @Test
    void testThrowExceptionIfIdOfFilmToUpdateIsNull() {
        Film updatedFilm = Film.builder().name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build();
        assertThrows(ValidationException.class, () -> filmService.updateFilm(updatedFilm));
    }

    @Test
    void testThrowExceptionWhenTryUpdateFilmWithNotExistId() {
        Film updatedFilm = Film.builder().id(10).name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build();
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(updatedFilm));
    }

    @Test
    void testCanGetFilmById() {
        Film film1 = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        Film film2 = filmService.getFilmById(1);
        assertEquals(film1.getName(), film2.getName());
    }

    @Test
    void testCanAddLikeToFilm() {
        Film film = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        User user = userService.addUser(User.builder().email("abv@mail.ru").login("Login")
                .birthday(LocalDate.of(1996, 12, 15)).build());
        filmService.addLike(film.getId(), user.getId());
        assertEquals(1, likeStorage.getIdsOfUserLikes(film.getId()).size());
    }

    @Test
    void testCanDeleteLikeFromFilm() {
        Film film = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        User user = userService.addUser(User.builder().email("abv@mail.ru").login("Login")
                .birthday(LocalDate.of(1996, 12, 15)).build());
        filmService.addLike(film.getId(), user.getId());
        filmService.deleteLike(film.getId(), user.getId());
        assertTrue(likeStorage.getIdsOfUserLikes(film.getId()).isEmpty());
    }

    @Test
    void testCanGetMostPopularFilms() {
        Film film1 = filmService.addFilm(Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                .mpa(MPA.builder().id(1).build()).build());
        User user = userService.addUser(User.builder().email("abv@mail.ru").login("Login")
                .birthday(LocalDate.of(1996, 12, 15)).build());
        filmService.addLike(film1.getId(), user.getId());
        Film film2 = filmService.addFilm(Film.builder().name("name1").description("description1")
                .releaseDate(LocalDate.of(2019, 10, 20)).duration(90)
                .mpa(MPA.builder().id(2).build()).build());
        List<Film> popularFilms = filmService.getMostPopularFilms(10,
                Optional.empty(), Optional.empty());
        assertEquals(film1.getName(), popularFilms.getFirst().getName());
    }

    @Test
    void testCantAddFilmWithUnknownMpa() {
        assertThrows(ValidationException.class, () -> {
            filmService.addFilm(Film.builder().name("name")
                    .description("description")
                    .releaseDate(LocalDate.of(2020, 10, 20)).duration(90)
                    .mpa(MPA.builder().id(10).build()).build());
        });
    }
}