package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

@Repository
@Primary
@Slf4j
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_NEW_USER_QUERY = "INSERT INTO users (email, login, user_name, birthday) " +
            "VALUES (?,?,?,?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, user_name = ?, " +
            "birthday = ? WHERE user_id = ?";
    private static final String GET_USER_FRIENDS_QUERY = "SELECT * FROM users " +
            "WHERE user_id IN (SELECT user_id2 FROM friendship WHERE user_id1 = ?)";
    private static final String GET_MUTUAL_FRIENDS_QUERY = "SELECT * FROM users u, friendship f, friendship o " +
            "WHERE u.user_id = f.user_id2 AND u.user_id = o.user_id2 AND f.user_id1 = ? AND o.user_id1 = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<User> getUsers() {
        return findAll(GET_ALL_USERS_QUERY);
    }

    @Override
    public User getUserById(Integer id) {
        log.info("поиск пльзователя по id из БД");
        return findOne(GET_USER_BY_ID_QUERY, id);
    }

    @Override
    public User addUser(User user) {
        log.info("добавляем пользователя в бд");
        int id = insert(INSERT_NEW_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        log.info("пользователь {} успешно добален", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("обновляем юзера {} в базе", user);
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public void deleteUserById(Integer userId) {
        delete(DELETE_USER_QUERY, userId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        return findAll(GET_USER_FRIENDS_QUERY, id);
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        return findAll(GET_MUTUAL_FRIENDS_QUERY, id, otherId);
    }
}
