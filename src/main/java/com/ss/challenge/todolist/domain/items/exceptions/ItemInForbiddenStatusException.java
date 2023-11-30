package com.ss.challenge.todolist.domain.items.exceptions;

public class ItemInForbiddenStatusException extends RuntimeException {
    public ItemInForbiddenStatusException(String message) {
        super(message);
    }
}
