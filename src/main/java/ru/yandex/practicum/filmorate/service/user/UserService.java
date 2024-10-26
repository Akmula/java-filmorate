package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public User addFriend(Integer userId, Integer friendId) {
        validate(userId, friendId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователь с id - {}, добавил в друзтья пользователя с id - {}.", userId, friendId);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        validate(userId, friendId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователь с id - {}, удалил из друзей пользователя с id - {}.", userId, friendId);
        return user;
    }

    public List<User> getFriends(Integer id) {
        User user = userStorage.getUserById(id);
        log.info("Ответ на запрос получения друзей пользователя с id - {}", id);
        return user.getFriends().stream().map(userStorage::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherFriends = other.getFriends();
        log.info("Ответ на запрос получения общих друзей пользователя с id - {}," +
                " с пользователем id - {}", userId, otherId);
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public void validate(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(friendId);
        }
    }
}