package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String CREATE_USER_QUERY = """
            INSERT INTO USERS
            (login, email, name, birthday)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_USER_QUERY = """
            UPDATE USERS
            SET login = ?, email = ?, name = ?, birthday = ?
            WHERE user_id = ?
            """;

    private static final String DELETE_USER_QUERY = """
            DELETE FROM USERS
            WHERE user_id = ?
            """;

    private static final String GET_ALL_USERS_QUERY = """
            SELECT * FROM USERS
            """;

    private static final String GET_USER_BY_ID_QUERY = GET_ALL_USERS_QUERY + """
            WHERE user_id = ?
            """;

    private static final String GET_USER_BY_LOGIN_QUERY = GET_ALL_USERS_QUERY + """
            WHERE login = ?
            """;

    private static final String GET_USER_BY_EMAIL_QUERY = GET_ALL_USERS_QUERY + """
            WHERE email = ?
            """;

    private static final String GET_USER_FRIENDS_QUERY = GET_ALL_USERS_QUERY + """
            WHERE user_id IN (
            SELECT friend_id
            FROM FRIENDSHIP
            WHERE user_id = ?)
            """;

    private static final String GET_COMMON_FRIENDS_QUERY = """
            SELECT * FROM USERS u
            JOIN FRIENDSHIP f
            ON u.user_id = f.friend_id
            WHERE f.user_id = ?
            AND f.friend_id
            IN (SELECT friend_id
            FROM FRIENDSHIP
            WHERE user_id = ?)
            """;

    public UserRepository(JdbcTemplate jdbcTemplate, RowMapper<User> userRowMapper) {
        super(jdbcTemplate, userRowMapper);
    }

    @Override
    public User createUser(User user) {
        log.info("UserRepository - Добавление пользователя {} в базу", user);
        if (user.getName() == null) {
            user.setName(user.getLogin().trim());
        }
        Integer id = insertToDatabase(
                CREATE_USER_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        log.info("UserRepository - Пользватель {} добавлен в базу данных", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("UserRepository - Обновление пользователя {} в базе", user);
        update(
                UPDATE_USER_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        log.info("UserRepository - Пользователь {} обновлен", user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        log.info("UserRepository - Удаление пользователя {} из базы", user);
        update(DELETE_USER_QUERY, user.getId());
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        log.info("UserRepository - Получение пользователя из базы по id - {}", userId);
        Optional<User> user = getOne(GET_USER_BY_ID_QUERY, userId);
        log.info("UserRepository - Получен пользователь по id - {}", user);
        return user;
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        log.info("UserRepository - Получение пользователя из базы по логину - {}", login);
        Optional<User> user = getOne(GET_USER_BY_LOGIN_QUERY, login);
        log.info("UserRepository - Получен пользователь по логину- {}", user);
        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("UserRepository - Получение пользователя из базы по email - {}", email);
        Optional<User> user = getOne(GET_USER_BY_EMAIL_QUERY, email);
        log.info("UserRepository - Получен пользователь email - {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("UserRepository - Получение пользователей из базы");
        return getAll(GET_ALL_USERS_QUERY);
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        log.info("UserRepository - Получение друзей из базы");
        if (getOne(GET_USER_BY_ID_QUERY, userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return getAll(GET_USER_FRIENDS_QUERY, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        log.info("UserRepository - Получение общих друзей из базы");
        return getAll(GET_COMMON_FRIENDS_QUERY, userId, otherId);
    }
}