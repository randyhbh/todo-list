package com.ss.challenge.todolist.TestUtils;

import com.ss.challenge.todolist.domain.items.Item;

import java.time.LocalDateTime;

public class MotherObject {
    public static Item createItem(String description, LocalDateTime createdAt, LocalDateTime dueAt) {
        return Item.createItem(description, createdAt, dueAt);
    }

    public static Item createDoneItem(String description, LocalDateTime createdAt, LocalDateTime dueAt, LocalDateTime completedAt) {
        var item = createItem(description, createdAt, dueAt);
        item.markCompleted(completedAt);
        return item;
    }

    public static Item createPastDueItem(String description, LocalDateTime createdAt, LocalDateTime dueAt) {
        var item = createItem(description, createdAt, dueAt);
        item.markPastDue();
        return item;
    }
}
