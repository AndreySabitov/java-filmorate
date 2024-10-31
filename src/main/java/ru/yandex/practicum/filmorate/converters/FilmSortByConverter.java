package ru.yandex.practicum.filmorate.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.FilmSortBy;

@Component
public class FilmSortByConverter implements Converter<String, FilmSortBy> {
    @Override
    public FilmSortBy convert(String source) {
        if (source.equals("year")) {
            return FilmSortBy.YEAR;
        } else {
            return FilmSortBy.LIKES;
        }
    }
}
