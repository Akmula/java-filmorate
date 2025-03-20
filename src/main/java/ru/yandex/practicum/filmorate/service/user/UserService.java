package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exceptions.DuplicateFoundException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.EventType.FRIEND;
import static ru.yandex.practicum.filmorate.model.Operation.ADD;
import static ru.yandex.practicum.filmorate.model.Operation.REMOVE;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final EventStorage eventStorage;
    private final RecommendationService recommendationService;

    public UserService(@Qualifier("userRepository") UserStorage userStorage, FriendshipStorage friendshipStorage,
                       EventStorage eventStorage, RecommendationService recommendationService) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.eventStorage = eventStorage;
        this.recommendationService = recommendationService;
    }

    public UserDto createUser(UserRequest request) {
        log.info("UserService - Добавление пользователя: {}", request);
        validateUserRequest(request);
        User user = UserMapper.mapToUser(request);
        UserDto userDto = UserMapper.mapToUserDto(userStorage.createUser(user));
        log.info("UserService - Добавлен пользователь - {}", userDto);
        return userDto;
    }

    public UserDto updateUser(UserRequest request) {
        log.info("UserService - Обновление пользователя: {}", request);
        validateUserRequest(request);
        User updatedUser = userStorage.getUserById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new UserNotFoundException(request.getId()));
        UserDto userDto = UserMapper.mapToUserDto(userStorage.updateUser(updatedUser));
        log.info("UserService - Обновленный пользователь: {}", userDto);
        return userDto;
    }

    public UserDto deleteUser(Integer userId) {
        log.info("UserService - Удаление пользователя с id: {}", userId);
        User deletedUser = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.deleteUser(deletedUser);
        UserDto userDto = UserMapper.mapToUserDto(deletedUser);
        log.info("UserService - Удаленный пользователь: {}", userDto);
        return userDto;
    }

    public Collection<UserDto> getAllUsers() {
        log.info("UserService - Получение всех пользователей");
        Collection<UserDto> usersDto = UserMapper.mapToUserDtoList(userStorage.getAllUsers());
        Collection<Friendship> friendships = friendshipStorage.getFriendship();

        usersDto.forEach(userDto -> {
            Set<Integer> friendshipIds = friendships.stream()
                    .filter(friendship -> friendship.getUserId().equals(userDto.getId()))
                    .map(Friendship::getFriendId)
                    .collect(Collectors.toSet());
            userDto.setFriends(friendshipIds);
        });
        log.info("UserService - Список всех пользователей получен");
        return usersDto;
    }

    public UserDto getUserById(Integer userId) {
        log.info("UserService - Получение пользователя по id - {}", userId);
        UserDto userDto = userStorage.getUserById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userDto.setFriends(friendshipStorage.getFriendsIds(userId));
        log.info("UserService - Получен пользователь - {}", userDto);
        return userDto;
    }

    public UserDto addFriend(Integer userId, Integer friendId) {
        log.info("UserService - Пользователь с id - {}, добавил в друзья пользователя с id - {}.", userId, friendId);
        checkUser(friendId);
        UserDto userDto = checkUser(userId);
        Boolean isFriend = validateFriendship(userId, friendId);
        friendshipStorage.addFriend(userId, friendId, isFriend);
        eventStorage.createEvent(userId, ADD, FRIEND, friendId);
        log.info("UserService - Пользователь с id - {}, добавлен в друзья пользователя с id - {}.", friendId, userId);
        return userDto;
    }

    public Collection<UserDto> getFriends(Integer userId) {
        log.info("UserService - Получение друзей пользователя с id - {}", userId);
        Collection<UserDto> friendsDto = UserMapper.mapToUserDtoList(userStorage.getUserFriends(userId));
        log.info("UserService - Получен список друзей: {}", friendsDto);
        return friendsDto;
    }

    public UserDto deleteFriend(Integer userId, Integer friendId) {
        log.info("UserService - Удаление из друзей пользователя с id - {}", friendId);
        checkUser(userId);
        UserDto friendDto = checkUser(friendId);
        friendshipStorage.deleteFriend(userId, friendId);
        Integer friendshipId = friendshipStorage.getFriendshipId(userId, friendId);
        friendshipStorage.updateFriendship(friendshipId, false);
        eventStorage.createEvent(userId, REMOVE, FRIEND, friendId);
        log.info("UserService - Пользователь с id - {}, удалил из друзей пользователя - {}.", userId, friendDto);
        return friendDto;
    }

    public Collection<UserDto> getCommonFriends(Integer userId, Integer otherId) {
        log.info("UserService - Получение общих друзей пользователя с id - {}," +
                 " с пользователем id - {}", userId, otherId);
        Collection<UserDto> friendsDto = UserMapper.mapToUserDtoList(userStorage.getCommonFriends(userId, otherId));
        log.info("UserService - Получен список общих друзей - {}", friendsDto);
        return friendsDto;
    }

    public Collection<EventDto> getFeed(Integer userId) {
        log.info("UserService - Получение событий пользователя с id - {}!", userId);
        Collection<EventDto> eventsDto = EventMapper.mapToEventDtoList(eventStorage.getEventsByUserId(userId));
        log.info("UserService - Получен список событий пользователя - {}", eventsDto);
        return eventsDto;
    }

    public Collection<FilmDto> getRecommendations(Integer userId) {
        log.info("UserService - Получение рекомендаций для пользователя с id - {}!", userId);
        Collection<Film> films = recommendationService.getRecommendations(userId);
        log.info("UserService - Получены рекомендации для пользователя - {}", films);
        return films.stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    private UserDto checkUser(Integer userId) {
        log.info("UserService - поиск пользователя в базе");
        return userStorage.getUserById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private boolean validateFriendship(Integer userId, Integer friendId) {
        log.info("UserService - валидация дружбы между {} и id - {}", userId, friendId);
        if (userId.equals(friendId)) {
            throw new InternalServerException("Нельзя себя добавить в друзья");
        }

        boolean isFriend = false;
        Set<Integer> userFriendsIds = friendshipStorage.getFriendsIds(userId);
        Set<Integer> friendFriendsIds = friendshipStorage.getFriendsIds(friendId);

        if (userFriendsIds.contains(friendId)) {
            throw new DuplicateFoundException("Пользователь с id - " + friendId + " , уже добавлен в друзья");
        }

        if (friendFriendsIds.contains(userId)) {
            isFriend = true;
            Integer friendshipId = friendshipStorage.getFriendshipId(userId, friendId);
            friendshipStorage.updateFriendship(friendshipId, true);
        }
        return isFriend;
    }

    private void validateUserRequest(UserRequest request) {
        log.info("UserService - валидация запроса {}", request);
        if (request.getLogin().contains(" ") || request.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidationException("Почта должна быть указана");
        }

        if (!request.getEmail().contains(".")) {
            throw new ValidationException("Неверный формат почты");
        }

        Optional<User> alreadyExistUser = userStorage.getUserByLogin(request.getLogin());
        if (alreadyExistUser.isPresent()) {
            throw new ValidationException("Данный логин уже используется");
        }

        alreadyExistUser = userStorage.getUserByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new ValidationException("Данная почта уже используется");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
    }
}