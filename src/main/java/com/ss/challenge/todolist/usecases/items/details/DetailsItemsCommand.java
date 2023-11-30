package com.ss.challenge.todolist.usecases.items.details;

import com.ss.challenge.todolist.domain.items.ItemStatus;
import lombok.Getter;

@Getter
public class DetailsItemsCommand {
    private final Long id;

    private DetailsItemsCommand(Long id) {
        this.id = id;
    }

    public static DetailsItemsCommand fromRequest(Long id) {
        return new DetailsItemsCommand(id);
    }
}
