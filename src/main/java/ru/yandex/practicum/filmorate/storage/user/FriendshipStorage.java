package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;
import java.util.Set;

public interface FriendshipStorage {

    void addFriend(Integer userId, Integer friendId, Boolean isFriend);

    void deleteFriend(Integer userId, Integer friendId);

    Collection<Friendship> getFriendship();

    Set<Integer> getFriendsIds(Integer userId);

    Integer getFriendshipId(Integer userId, Integer friendId);

    void updateFriendship(Integer friendshipId, Boolean isFriend);
}