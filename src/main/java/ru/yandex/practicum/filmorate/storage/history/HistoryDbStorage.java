package ru.yandex.practicum.filmorate.storage.history;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;

@Slf4j
@Repository
@Primary
public class HistoryDbStorage extends BaseDbStorage<Event> {
    private static final String INSERT_QUERY = "INSERT INTO events_feed (" +
            "user_id, time_action, event_type, operation, entity_id) VALUES (?,?,?,?,?)";
    private static final String FIND_EVENTS_BY_USER_ID_QUERY = "SELECT * FROM events_feed WHERE user_id = ?";

    public HistoryDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Event> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Event addEvent(Event event) {
        long eventId = insert(
                INSERT_QUERY,
                event.getUserId(),
                event.getTimestamp(),
                event.getEventType().toString(),
                event.getOperationType().toString(),
                event.getEntityId()
        );
        event.setEventId(eventId);
        log.info("Создан новый ЭВЕНТ с ID {}. Добавлено действие {} {} = {} пользователя с ID = {}.",
                event.getEventId(), event.getEventType().toString(), event.getOperationType().toString(),
                event.getEntityId(), event.getUserId());
        return event;
    }

    public Collection<Event> findEventsByUserId(Integer userId) {
        log.info("Получение всех ЭВЕНТов пользователя с ID = {}.", userId);
        return findAll(FIND_EVENTS_BY_USER_ID_QUERY, userId);
    }
}
