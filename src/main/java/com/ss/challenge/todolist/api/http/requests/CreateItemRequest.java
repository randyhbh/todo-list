package com.ss.challenge.todolist.api.http.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateItemRequest(
        @NotNull
        @NotEmpty
        String description,
        @NotNull
        @Future(message = "The Item due date can not be in the past")
        LocalDateTime dueAt
) {
}