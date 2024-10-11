package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.rating.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<MPA> getRatings() {
        return mpaStorage.getRatings();
    }

    public MPA getRatingById(Integer id) {
        return mpaStorage.getRatingById(id);
    }
}
