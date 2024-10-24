package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films ";
    private static final String GET_FILM_BY_ID_QUERY = GET_ALL_FILMS_QUERY.concat("WHERE film_id = ?");
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (title, description, release_date, duration, " +
            "rating_id) VALUES(?,?,?,?,?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET title = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String GET_MOST_POPULAR_FILMS_QUERY =
            "SELECT f.FILM_ID AS FILM_ID, TITLE, DESCRIPTION, RELEASE_DATE, DURATION, rating_id FROM FILMS f " +
                    "LEFT JOIN USER_LIKES ul ON f.FILM_ID = ul.FILM_ID " +
                    "GROUP BY f.FILM_ID ORDER BY COUNT(user_id) DESC " +
                    "LIMIT ?";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String INSERT_FILM_DIRECTORS_QUERY = "INSERT INTO films_directors (film_id, director_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String GET_RECOMMENDED_FILMS = GET_ALL_FILMS_QUERY
            .concat("""
                    f WHERE FILM_ID IN (SELECT UL2.FILM_ID
                                        FROM USER_LIKES ul2
                                        WHERE USER_ID = (SELECT USER_ID
                                                         FROM USER_LIKES ul
                                                         WHERE FILM_ID IN (SELECT film_id
                                                                           FROM USER_LIKES ul
                                                                           WHERE USER_ID = ?)
                                                           AND USER_ID != ?
                                                         GROUP BY USER_ID
                                                         ORDER BY COUNT(FILM_ID) DESC
                                                         LIMIT 1)
                                        EXCEPT
                                        SELECT ul.FILM_ID
                                        FROM USER_LIKES ul
                                        WHERE USER_ID = ?);""");
    private static final String GET_FILMS_BY_DIRECTOR = "SELECT f.film_id, title, description, release_date , duration, " +
            "rating_id FROM films AS f LEFT OUTER JOIN (SELECT film_id, " +
            "count(user_id) AS likes FROM user_likes GROUP BY film_id) AS ul ON f.film_id = ul.film_id WHERE f.film_id IN " +
            "(SELECT film_id FROM films_directors WHERE director_id = ?) ORDER BY ";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Film> getFilms() {
        return findAll(GET_ALL_FILMS_QUERY);
    }

    @Override
    public Film getFilmById(Integer id) {
        try {
            return findOne(GET_FILM_BY_ID_QUERY, id);
        } catch (Exception e) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public int addFilm(Film film) {
        log.info("добавляем фильм {} в бд", film);
        int id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        log.info("добавляем жанры");
        Set<Integer> genresIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        genresIds.forEach(integer -> update(INSERT_FILM_GENRES_QUERY, id, integer));
        log.info("жанры добавлены");
        log.info("добавляем ружиссеров");
        Set<Integer> directorsIds = film.getDirectors().stream().map(Director::getId).collect(Collectors.toSet());
        directorsIds.forEach(integer -> update(INSERT_FILM_DIRECTORS_QUERY, id, integer));
        log.info("режиссеры добавлены");
        return id;
    }

    @Override
    public void updateFilm(Film film) {
        int id = film.getId();
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id
        );
        film.getGenres().stream().map(Genre::getId)
                .forEach(integer -> update(INSERT_FILM_GENRES_QUERY, id, integer));
        film.getDirectors().stream().map(Director::getId).collect(Collectors.toSet())
                .forEach(integer -> update(INSERT_FILM_DIRECTORS_QUERY, id, integer));
    }

    @Override
    public void deleteFilm(Integer filmId) {
        log.info("удаляем фильм из БД");
        delete(DELETE_FILM_QUERY, filmId);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return findAll(GET_MOST_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public List<Film> getRecommendedFilms(Integer id) {
        return findAll(GET_RECOMMENDED_FILMS, id, id, id);
    }

    @Override
    public List<Film> getFilmsByDirector(Integer dirId, String sortBy) {
        String orderBy;
        if (sortBy.equals("year")) {
            orderBy = "release_date";
        } else {
            orderBy = "likes DESC";
        }
        return findAll(GET_FILMS_BY_DIRECTOR + orderBy, dirId);
    }

}
