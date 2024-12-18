package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class FriendshipRepository extends BaseRepository<Friendship> implements FriendshipStorage {

    private static final String INSERT_FRIEND_QUERY = """
            INSERT INTO FRIENDSHIP
            (user_id, friend_id, is_friend)
            VALUES (?, ?, ?)
            """;

    private static final String GET_FRIENDS_ID_QUERY = """
            SELECT friend_id
            FROM FRIENDSHIP
            WHERE user_id = ?
            """;

    private static final String GET_FRIENDS_QUERY = """
            SELECT * FROM FRIENDSHIP
            WHERE user_id = ?
            AND friend_id = ?
            """;

    private static final String UPDATE_IS_FRIENDS_QUERY = """
            UPDATE FRIENDSHIP
            SET is_friend = ?
            WHERE friendship_id = ?
            """;

    private static final String DELETE_FRIENDSHIP_QUERY = """
            DELETE FROM FRIENDSHIP
            WHERE user_id = ?
            AND friend_id = ?
            """;

    public FriendshipRepository(JdbcTemplate jdbcTemplate, RowMapper<Friendship> friendshipRowMapper) {
        super(jdbcTemplate, friendshipRowMapper);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId, Boolean isFriend) {
        log.info("FriendshipRepository - Добавление пользователя с id - {} в друзья", friendId);
        insertToDatabase(INSERT_FRIEND_QUERY, userId, friendId, isFriend);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("FriendshipRepository - Удаление пользователя с id - {} из друзей", friendId);
        delete(DELETE_FRIENDSHIP_QUERY, userId, friendId);
    }

    @Override
    public Set<Integer> getFriendsIds(Integer userId) {
        log.info("FriendshipRepository - Получение друзей пользователя по id - {}", userId);
        return new HashSet<>(getAsList(GET_FRIENDS_ID_QUERY, userId));
    }

    @Override
    public Integer getFriendshipId(Integer userId, Integer friendId) {
        log.info("FriendshipRepository - Получение идентификатора дружбы из базы по id - {}", userId);
        Optional<Friendship> friendship = getOne(GET_FRIENDS_QUERY, friendId, userId);
        return friendship.map(Friendship::getFriendshipId).orElse(null);
    }

    @Override
    public void updateFriendship(Integer friendshipId, Boolean isFriend) {
        log.info("FriendshipRepository - Обновление состояния дружбы у пользователя с id - {}", friendshipId);
        update(UPDATE_IS_FRIENDS_QUERY, isFriend, friendshipId);
    }
}