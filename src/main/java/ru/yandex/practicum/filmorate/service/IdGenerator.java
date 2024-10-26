package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class IdGenerator {
    private Integer filmId = 0;
    private Integer userId = 0;

    public Integer getNextFilmId() {
        return ++filmId;
    }

    public Integer getNextUserId() {
        return ++userId;
    }
}