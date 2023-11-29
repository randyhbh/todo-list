package com.ss.challenge.todolist.domain.items;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_gen", sequenceName = "item_seq")
    private Long id;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime dueAt;

    private LocalDateTime doneAt;

    public static Item createItem(String description, LocalDateTime createdAt, LocalDateTime dueAt) {
        return new Item()
                .setStatus(ItemStatus.NOT_DONE)
                .setCreatedAt(createdAt)
                .setDescription(description)
                .setDueAt(dueAt);
    }

    private Item setStatus(ItemStatus status) {
        this.status = status;
        return this;
    }

    private Item setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Item description cannot be NULL");

        if (description.isBlank())
            throw new IllegalArgumentException("Item description cannot be Empty or only have spaces");

        this.description = description;
        return this;
    }

    private Item setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    private Item setDueAt(LocalDateTime dueAt) {
        if (dueAt == null)
            throw new IllegalArgumentException("Item dueAt date cannot be NULL");

        if (dueAt.isBefore(this.createdAt))
            throw new IllegalArgumentException("Item due date needs to be after the creation date");

        if (dueAt.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Item dueAt date needs to be a date in the future");

        this.dueAt = dueAt;
        return this;
    }

    private Item setDoneAt(LocalDateTime doneAt) {
        this.doneAt = doneAt;
        return this;
    }
}
