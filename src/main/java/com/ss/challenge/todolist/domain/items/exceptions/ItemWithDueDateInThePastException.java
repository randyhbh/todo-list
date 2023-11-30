package com.ss.challenge.todolist.domain.items.exceptions;

public class ItemWithDueDateInThePastException extends RuntimeException {
    public ItemWithDueDateInThePastException(String message) {
        super(message);
    }
}
