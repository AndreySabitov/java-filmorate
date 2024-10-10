package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.filmLikes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.rating.MpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreDbStorage;
    private final MpaStorage mpaStorage;

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        log.info("получили фильмы");
        Map<Integer, List<Integer>> filmLikesMap = new HashMap<>();
        likeStorage.getAllLikes().forEach(userLikes -> {
            if (!filmLikesMap.containsKey(userLikes.getFilmId())) {
                filmLikesMap.put(userLikes.getFilmId(), List.of(userLikes.getUserId()));
            } else {
                List<Integer> likes = new ArrayList<>(filmLikesMap.get(userLikes.getFilmId()));
                likes.add(userLikes.getUserId());
                filmLikesMap.put(userLikes.getFilmId(), likes);
            }
        });
        log.info("получили таблицу лайков {}", filmLikesMap);
        films.forEach(film -> {
            if (!filmLikesMap.get(film.getId()).contains(0)) {
                film.getIdsOfUsersLikes().addAll(filmLikesMap.get(film.getId()));
            }
        });
        log.info("добавили лайки к каждому фильму");
        return films;
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        List<Genre> genres = genreDbStorage.getGenresOfFilm(id);
        film.getGenres().addAll(genres);
        film.setMpa(mpaStorage.getRatingOfFilm(id));
        return film;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        return setNamesOfGenresAndMpa(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (film.getId() == null) {
            log.error("Не задан id");
            throw new ValidationException("id должен быть задан!");
        }
        Film film1 = filmStorage.updateFilm(film);
        return setNamesOfGenresAndMpa(film1);
    }

    public Film addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
        return filmStorage.getFilmById(id);
    }

    public Film deleteLike(Integer id, Integer userId) {
        likeStorage.deleteLike(id, userId);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getMostPopularFilms(count);
    }

    public List<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

    public Genre getGenreById(Integer id) {
        return genreDbStorage.getGenreById(id);
    }

    public List<MPA> getRatings() {
        return mpaStorage.getRatings();
    }

    public MPA getRatingById(Integer id) {
        return mpaStorage.getRatingById(id);
    }

    private Film setNamesOfGenresAndMpa(Film film) {
        List<Genre> genres = film.getGenres().stream()
                .map(genre -> Genre.builder().id(genre.getId()).name(getGenreName(genre.getId())).build())
                .toList();
        film.getMpa().setName(getMpaName(film.getMpa().getId()));
        Film newFilm = Film.builder().name(film.getName()).mpa(film.getMpa()).duration(film.getDuration())
                .releaseDate(film.getReleaseDate()).id(film.getId()).description(film.getDescription()).build();
        newFilm.getGenres().addAll(genres);
        return newFilm;
    }

    private String getGenreName(Integer id) {
        return switch (id) {
            case 1 -> "Комедия";
            case 2 -> "Драма";
            case 3 -> "Мультфильм";
            case 4 -> "Триллер";
            case 5 -> "Документальный";
            case 6 -> "Боевик";
            default -> throw new ValidationException("некорректный id жанра");
        };
    }

    private String getMpaName(Integer id) {
        return switch (id) {
            case 1 -> "G";
            case 2 -> "PG";
            case 3 -> "PG-13";
            case 4 -> "R";
            case 5 -> "NC-17";
            default -> throw new ValidationException("некорректный id возрастного рейтинга");
        };
    }

    private void validateFilm(Film film) {
        log.info("Начало валидации информации о добавляемом фильме");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации:Дата релиза должна быть не раньше 28.12.1895г");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895г");
        }
        film.getGenres().stream().map(Genre::getId).forEach(this::getGenreName);
        getMpaName(film.getMpa().getId());
        log.info("Валидация прошла успешно");
    }
}
