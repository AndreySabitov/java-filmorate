package ru.yandex.practicum.filmorate.storage.user.friendship;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Friendship {
    private final Integer userId;
    private final Integer friendId;
}
