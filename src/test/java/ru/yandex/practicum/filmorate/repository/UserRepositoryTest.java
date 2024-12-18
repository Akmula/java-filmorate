package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru/yandex/practicum/filmorate/*")
public class UserRepositoryTest {
    private final UserRepository userStorage;
    private final FriendshipRepository friendshipStorage;
    User newUser;
    User newFriend;

    @BeforeEach
    public void setUp() {
        newUser = User.builder()
                .login("Test user")
                .email("test@test.com")
                .name("Test user")
                .birthday(LocalDate.of(1980, 12, 12))
                .build();

        newFriend = User.builder()
                .login("Test friend")
                .email("friend@friend.com")
                .name("Test friend")
                .birthday(LocalDate.of(1980, 12, 12))
                .build();
    }

    @Test
    public void testCreateUser() {
        userStorage.createUser(newUser);
        final int userId = newUser.getId();
        Optional<User> userOptional = userStorage.getUserById(userId);
        assertThat(userOptional)
                .isPresent().hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "Test user")
                );
    }

    @Test
    public void testUpdateUser() {
        userStorage.createUser(newUser);
        newUser.setLogin("Updated user");
        userStorage.updateUser(newUser);
        final int userId = newUser.getId();
        Optional<User> userOptional = userStorage.getUserById(userId);
        assertThat(userOptional)
                .isPresent().hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "Updated user")
                );
    }

    @Test
    public void testGetUserById() {
        userStorage.createUser(newUser);
        final int userId = newUser.getId();
        Optional<User> userOptional = userStorage.getUserById(userId);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userId)
                );
    }

    @Test
    public void testGetAllUsers() {
        userStorage.createUser(newUser);
        userStorage.createUser(newFriend);
        Collection<User> users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void testGetCommonFriends() {
        userStorage.createUser(newUser);
        userStorage.createUser(newFriend);
        final int userId = newUser.getId();
        final int friendId = newFriend.getId();

        friendshipStorage.addFriend(userId, friendId, false);
        Set<Integer> friendsIds = friendshipStorage.getFriendsIds(userId);
        Integer id = friendsIds.stream().findFirst().orElse(null);
        assert id != null;
        assertEquals(id, friendId);
    }
}