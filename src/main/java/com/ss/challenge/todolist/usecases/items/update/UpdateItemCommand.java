package com.ss.challenge.todolist.usecases.items.update;

import com.ss.challenge.todolist.api.http.requests.UpdateItemDescriptionRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateItemCommand {
    @NotNull
    private final Long itemId;
    @NotNull
    @NotEmpty
    private final String description;

    private UpdateItemCommand(Long id, String description) {
        this.itemId = id;
        this.description = description;
    }

    public static UpdateItemCommand fromRequest(@NotNull UpdateItemDescriptionRequest request, Long id) {
        return new UpdateItemCommand(id, request.description());
    }
}
