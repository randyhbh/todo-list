package com.ss.challenge.todolist.api.http.responses;

import com.ss.challenge.todolist.domain.items.ItemStatus;

import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String description,
        LocalDateTime dueAt,
        ItemStatus status
) {
}
