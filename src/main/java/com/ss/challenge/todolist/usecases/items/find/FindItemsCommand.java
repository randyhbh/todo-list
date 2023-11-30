package com.ss.challenge.todolist.usecases.items.find;

import com.ss.challenge.todolist.domain.items.ItemStatus;
import lombok.Getter;

@Getter
public class FindItemsCommand {
    private final ItemStatus[] statuses;

    private FindItemsCommand(boolean includeAllStatuses) {
        this.statuses = includeAllStatuses ? ItemStatus.values() : new ItemStatus[]{ItemStatus.NOT_DONE};
    }

    public static FindItemsCommand fromRequest(boolean includeAllStatuses) {
        return new FindItemsCommand(includeAllStatuses);
    }
}
