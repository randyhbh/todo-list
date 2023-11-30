package com.ss.challenge.todolist.domain.items;

import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import com.ss.challenge.todolist.domain.items.exceptions.ItemWithDueDateInThePastException;
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

    public Item updateDescription(String description) {
        checkIfStatusIsNotPastDueOrThrow();
        return this.setDescription(description);
    }

    public Item markCompleted(LocalDateTime completedAt) {
        checkIfStatusIsNotPastDueOrThrow();
        return this.setStatus(ItemStatus.DONE).setDoneAt(completedAt);
    }

    public Item reOpen(LocalDateTime reOpenAt) {
        checkIfStatusIsNotPastDueOrThrow();
        checkIfDueDateIsInTheFutureOrThrow(reOpenAt);

        if (this.status == ItemStatus.NOT_DONE)
            return this;

        return this.setStatus(ItemStatus.NOT_DONE).setDoneAt(null);
    }

    private void checkIfDueDateIsInTheFutureOrThrow(LocalDateTime reOpenAt) throws ItemWithDueDateInThePastException {
        if (this.dueAt.isBefore(reOpenAt))
            throw new ItemWithDueDateInThePastException("Item with 'id' " + this.id + " is past the due date and cannot be modified");
    }

    private void checkIfStatusIsNotPastDueOrThrow() throws ItemInForbiddenStatusException {
        if (this.status == ItemStatus.PAST_DUE)
            throw new ItemInForbiddenStatusException("Item with 'id' " + this.id + " has status " + ItemStatus.PAST_DUE + " and cannot be modified");
    }

    private void checkIfStatusIsNotNotDoneOrThrow() throws ItemInForbiddenStatusException {
        if (this.status != ItemStatus.NOT_DONE)
            throw new ItemInForbiddenStatusException("Item with 'id' " + this.id + " expected the status to be " + ItemStatus.NOT_DONE + " but " + this.status + " found");
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
