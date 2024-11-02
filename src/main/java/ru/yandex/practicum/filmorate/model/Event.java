package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

@Getter
@Setter
@Builder
public class Event {
    @NotNull
    private Integer eventId;
    @NotNull
    private Integer userId;
    @NotNull
    private long timestamp;
    @NotNull
    private EventType eventType;
    @NotNull
    private OperationType operation;
    @NotNull
    private Integer entityId;

}
