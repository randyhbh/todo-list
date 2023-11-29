package com.ss.challenge.todolist.usecases.items.create;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateItemCommand {
    @NotNull
    private final String description;
    @NotNull
    private final LocalDateTime dueAt;

    private CreateItemCommand(String description, LocalDateTime dueAt) {
        this.description = description;
        this.dueAt = dueAt;
    }

    public static CreateItemCommand fromRequest(@NotNull CreateItemRequest request) {
        return new CreateItemCommand(request.description(), request.dueAt());
    }
}
