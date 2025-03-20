package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getInt("event_id"))
                .userId(resultSet.getInt("user_id"))
                .operation(Operation.valueOf(resultSet.getString("operation")))
                .eventType(EventType.valueOf(resultSet.getString("event")))
                .entityId(resultSet.getInt("entity_id"))
                .timestamp(resultSet.getTimestamp("timestamp"))
                .build();
    }
}