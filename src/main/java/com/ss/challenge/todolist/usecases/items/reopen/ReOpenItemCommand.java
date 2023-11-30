package com.ss.challenge.todolist.usecases.items.reopen;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReOpenItemCommand {
    @NotNull
    private final Long itemId;

    private ReOpenItemCommand(Long id) {
        this.itemId = id;
    }

    public static ReOpenItemCommand fromRequest(Long id) {
        return new ReOpenItemCommand(id);
    }
}
