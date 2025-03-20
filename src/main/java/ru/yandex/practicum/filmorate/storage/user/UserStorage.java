package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    Optional<User> getUserById(Integer userId);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    Collection<User> getUserFriends(Integer userId);

    Collection<User> getCommonFriends(Integer userId, Integer otherId);

}