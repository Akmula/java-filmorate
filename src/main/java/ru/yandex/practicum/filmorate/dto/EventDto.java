package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

@Data
@Builder
public class EventDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer eventId;
    private Integer userId;
    private Operation operation;
    private EventType eventType;
    private Integer entityId;
    private Long timestamp;
}