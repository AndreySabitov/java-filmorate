package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.history.HistoryDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final HistoryDbStorage historyDbStorage;
    private final UserStorage userStorage;

    public List<Event> getFeedForUser(Integer userId) {
        userStorage.getUserById(userId);
        return new ArrayList<>(historyDbStorage.findEventsByUserId(userId));
    }
}
