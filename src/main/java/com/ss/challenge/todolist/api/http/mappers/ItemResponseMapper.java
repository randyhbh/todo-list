package com.ss.challenge.todolist.api.http.mappers;

import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.domain.items.Item;

public class ItemResponseMapper {
    public static ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getDescription(), item.getDueAt(), item.getStatus());
    }
}
