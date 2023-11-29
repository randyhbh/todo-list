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

    private Item setStatus(ItemStatus status) {
        this.status = status;
        return this;
    }

    private Item setDescription(String description) {
        this.description = description;
        return this;
    }

    private Item setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    private Item setDueAt(LocalDateTime dueAt) {
        this.dueAt = dueAt;
        return this;
    }

    private Item setDoneAt(LocalDateTime doneAt) {
        this.doneAt = doneAt;
        return this;
    }
}
