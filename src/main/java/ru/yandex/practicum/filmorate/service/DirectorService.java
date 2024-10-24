package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    public Director getDirectorById(Integer dirId) {
        return directorStorage.getDirectorById(dirId);
    }

    public Director addDirector(Director director) {
        int id = directorStorage.addDirector(director);
        return getDirectorById(id);
    }

    public Director updateDirector(Director director) {
        directorStorage.updateDirector(director);
        return getDirectorById(director.getId());
    }

    public void deleteDirector(Integer dirId) {
        directorStorage.deleteDirector(dirId);
    }
}
