package com.ss.challenge.todolist.usecases.items.complete;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompleteItemCommand {
    @NotNull
    private final Long itemId;

    private CompleteItemCommand(Long itemId) {
        this.itemId = itemId;
    }

    public static CompleteItemCommand fromRequest(Long id) {
        return new CompleteItemCommand(id);
    }
}
