package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

@Getter
@Setter
@ToString
@Builder
public class Event {
    @NotNull
    private long eventId;
    @NotNull
    private long userId;
    @NotNull
    private long timestamp;
    @NotNull
    private EventType eventType;
    @NotNull
    @JsonProperty("operation")
    private OperationType operationType;
    @NotNull
    private long entityId;

}
