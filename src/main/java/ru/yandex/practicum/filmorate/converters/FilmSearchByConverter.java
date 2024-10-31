package ru.yandex.practicum.filmorate.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.FilmSearchBy;

@Component
public class FilmSearchByConverter implements Converter<String, FilmSearchBy> {
    @Override
    public FilmSearchBy convert(String source) {
        if (source.equals("title")) {
            return FilmSearchBy.TITLE;
        } else if (source.equals("director")) {
            return FilmSearchBy.DIRECTOR;
        } else {
            return FilmSearchBy.TITLE_DIRECTOR;
        }
    }
}
