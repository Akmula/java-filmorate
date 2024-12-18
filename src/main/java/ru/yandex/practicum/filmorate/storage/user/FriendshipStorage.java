package ru.yandex.practicum.filmorate.storage.user;

import java.util.Set;

public interface FriendshipStorage {

    void addFriend(Integer userId, Integer friendId, Boolean isFriend);

    void deleteFriend(Integer userId, Integer friendId);

    Set<Integer> getFriendsIds(Integer userId);

    Integer getFriendshipId(Integer userId, Integer friendId);

    void updateFriendship(Integer friendshipId, Boolean isFriend);
}