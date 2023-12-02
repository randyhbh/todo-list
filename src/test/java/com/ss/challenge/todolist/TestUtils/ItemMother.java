package com.ss.challenge.todolist.TestUtils;

import com.ss.challenge.todolist.domain.items.Item;

import java.time.LocalDateTime;

public class ItemMother {
    public static Item create(String description, LocalDateTime createdAt, LocalDateTime dueAt) {
        return Item.createItem(description, createdAt, dueAt);
    }

    public static Item createCompleted(LocalDateTime createdAt, LocalDateTime dueAt, LocalDateTime completedAt) {
        var description = "Test Description";
        var item = create(description, createdAt, dueAt);
        item.markCompleted(completedAt);
        return item;
    }

    public static Item createPastDue(LocalDateTime createdAt, LocalDateTime dueAt) {
        var description = "Test Description";
        var item = create(description, createdAt, dueAt);
        item.markPastDue();
        return item;
    }
}
