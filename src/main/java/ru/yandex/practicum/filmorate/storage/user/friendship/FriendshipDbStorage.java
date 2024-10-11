package ru.yandex.practicum.filmorate.storage.user.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
public class FriendshipDbStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {

    private static final String INSERT_NEW_FRIENDSHIP_QUERY = "INSERT INTO friendship (user_id1, user_id2) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendship WHERE user_id1 = ? AND user_id2 = ?";
    private static final String GET_FRIENDSHIP_OF_USER_QUERY = "SELECT user_id2 FROM friendship " +
            "WHERE user_id1 = ?";
    private static final String GET_FRIENDSHIP_QUERY = "SELECT u.USER_ID AS user_id1, f.USER_ID2, f.STATUS_ID " +
            "FROM USERS u LEFT JOIN FRIENDSHIP f ON u.USER_ID = f.USER_ID1";


    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Friendship> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        update(INSERT_NEW_FRIENDSHIP_QUERY, id, friendId);
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
