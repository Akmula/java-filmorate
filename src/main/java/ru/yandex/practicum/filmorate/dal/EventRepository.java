package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;

import java.sql.Timestamp;
import java.util.Collection;

@Slf4j
@Repository
public class EventRepository extends BaseRepository<Event> implements EventStorage {

    private static final String CREATE_EVENT_QUERY = """
            INSERT INTO EVENTS (user_id, operation, event, entity_id, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String GET_EVENT_QUERY = """
            SELECT * FROM EVENTS
            WHERE user_id = ?
            """;

    public EventRepository(JdbcTemplate jdbcTemplate, RowMapper<Event> eventRowMapper) {
        super(jdbcTemplate, eventRowMapper);
    }

    @Override
    public void createEvent(Integer userId, Operation operation, EventType eventType, Integer entityId) {
        log.info("EventRepository - Добавление события пользователя с id: {} в базу", userId);

        Integer id = insertToDatabase(
                CREATE_EVENT_QUERY,
                userId,
                operation.name(),
                eventType.name(),
                entityId,
                new Timestamp(System.currentTimeMillis())
        );
        log.info("EventRepository - Событие с id: {}, добавлено в базу данных!", id);
    }

    @Override
    public Collection<Event> getEventsByUserId(Integer userId) {
        log.info("EventRepository - Получение событий пользователя из базы по id - {}", userId);
        Collection<Event> events = getAll(GET_EVENT_QUERY, userId);
        log.info("EventRepository - Получены события пользователя по id - {}", events);
        return events;
    }
}