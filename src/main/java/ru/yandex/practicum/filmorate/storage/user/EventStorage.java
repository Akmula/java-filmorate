package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.Collection;

public interface EventStorage {

    void createEvent(Integer userId, Operation operation, EventType eventType, Integer entityId);

    Collection<Event> getEventsByUserId(Integer userId);
}