package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class EventMapper {

    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .eventId(event.getEventId())
                .userId(event.getUserId())
                .operation(event.getOperation())
                .eventType(event.getEventType())
                .entityId(event.getEntityId())
                .timestamp(event.getTimestamp().toInstant().toEpochMilli())
                .build();
    }

    public static Collection<EventDto> mapToEventDtoList(Collection<Event> eventDtoCollection) {
        return eventDtoCollection.stream()
                .map(EventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }
}