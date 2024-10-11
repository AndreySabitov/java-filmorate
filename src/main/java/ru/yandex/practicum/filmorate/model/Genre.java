package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Genre implements Comparable<Genre> {
    private final Integer id;
    private final String name;

    // тесты постмана требуют, чтобы жанры выводились в порядке возрастания по id.
    // Если убрать сортировку, то они выводятся в порядке убывания
    @Override
    public int compareTo(Genre o) {
        return this.id - o.getId();
    }
}
