package ru.yandex.practicum.filmorate.storage.user.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
@Slf4j
public class FriendshipDbStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private static final String GET_FRIENDSHIP_STATUS_QUERY = "SELECT status_id FROM FRIENDSHIP f WHERE user_id1 = ?" +
            " AND user_id2 = ?";
    private static final String INSERT_NEW_FRIENDSHIP_QUERY = "INSERT INTO friendship (user_id1, user_id2, status_id) " +
            "VALUES (?, ?, ?)";
    private static final String UPDATE_FRIENDSHIP_STATUS_QUERY = "UPDATE friendship SET status_id = ? " +
            "WHERE user_id1 = ? AND user_id2 = ?";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendship WHERE user_id1 = ? AND user_id2 = ?";
    private static final String GET_FRIENDSHIP_OF_USER_QUERY = "SELECT user_id2 FROM friendship " +
            "WHERE user_id1 = ?";
    private static final String GET_FRIENDSHIP_QUERY = "SELECT u.USER_ID AS user_id1, f.USER_ID2, f.STATUS_ID " +
            "FROM USERS u LEFT JOIN FRIENDSHIP f ON u.USER_ID = f.USER_ID1";


    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Friendship> mapper) {
        super(jdbcTemplate, mapper);
    }

    private Integer getFriendshipStatus(Integer friendId, Integer id) {
        Integer statusId;
        try {
            statusId = jdbcTemplate.queryForObject(GET_FRIENDSHIP_STATUS_QUERY, Integer.class, friendId, id);
        } catch (Exception e) {
            statusId = null;
        }
        log.info("получили статус номер {}", statusId);
        if (statusId == null) {
            return 0;
        } else {
            return statusId;
        }
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        int result = getFriendshipStatus(friendId, id);
        if (result == 0) {
            log.info("односторонний запрос. Адресат появляется в друзьях у отправителя");
            update(INSERT_NEW_FRIENDSHIP_QUERY, id, friendId, 2);
        } else if (result == 2) {
            log.info("встречный запрос. Оба пользователя друг у друга в друзьях");
            update(INSERT_NEW_FRIENDSHIP_QUERY, id, friendId, 1);
            update(UPDATE_FRIENDSHIP_STATUS_QUERY, 1, friendId, id);
        }
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        delete(DELETE_FRIENDSHIP_QUERY, id, friendId);
    }

    @Override
    public List<Friendship> getFriendship() {
        return findAll(GET_FRIENDSHIP_QUERY);
    }

    @Override
    public List<Integer> getFriendshipOfUser(Integer id) {
        return jdbcTemplate.queryForList(GET_FRIENDSHIP_OF_USER_QUERY, Integer.class, id);
    }
}
