package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Integer eventId;
    private Integer userId;
    private Operation operation;
    private EventType eventType;
    private Integer entityId;
    Timestamp timestamp;
}