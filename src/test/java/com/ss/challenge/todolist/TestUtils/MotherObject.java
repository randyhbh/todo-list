package com.ss.challenge.todolist.TestUtils;

import com.ss.challenge.todolist.domain.items.Item;

import java.time.LocalDateTime;

public class MotherObject {
    public static Item createItem(String description, LocalDateTime createdAt, LocalDateTime dueAt) {
        return Item.createItem(description, createdAt, dueAt);
    }

}
