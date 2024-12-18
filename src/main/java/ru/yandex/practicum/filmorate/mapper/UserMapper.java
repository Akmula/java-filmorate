package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
public final class UserMapper {
    public static User mapToUser(UserRequest userRequest) {
        return User.builder()
                .login(userRequest.getLogin())
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .birthday(userRequest.getBirthday())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friends(user.getFriends())
                .build();
    }

    public static Collection<UserDto> mapToUserDtoList(Collection<User> users) {
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public static User updateUserFields(User user, UserRequest request) {
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        return user;
    }
}