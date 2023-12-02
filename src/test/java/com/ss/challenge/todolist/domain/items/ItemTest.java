package com.ss.challenge.todolist.domain.items;

import com.ss.challenge.todolist.TestUtils.MotherObject;
import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import com.ss.challenge.todolist.domain.items.exceptions.ItemWithDueDateInThePastException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    @Test
    void checkCreatingNewItemReturnsTheExpectedValues() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createItem(description, createdAt, dueAt);

        Assertions.assertThat(ItemStatus.NOT_DONE).isEqualTo(item.getStatus());
        Assertions.assertThat(item.getDescription()).isNotNull().isEqualTo(description);
        Assertions.assertThat(item.getDueAt()).isNotNull().isEqualTo(dueAt);
        Assertions.assertThat(item.getCreatedAt()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
        Assertions.assertThat(item.getDoneAt()).isNull();
        Assertions.assertThat(item.getId()).isNull();
    }

    @Test
    void checkUpdateItemDescriptionIsSuccessful() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createItem(description, createdAt, dueAt);

        String newDescription = "New description";
        item.updateDescription(newDescription);

        Assertions.assertThat(item.getDescription())
                .isNotNull()
                .isNotEqualTo(description)
                .isEqualTo(newDescription);
    }

    @Test
    void checkUpdateItemDescriptionThrowsExceptionForItemWithStatusPastDue() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createPastDueItem(description, createdAt, dueAt);

        String exceptionMessage = "Item with 'id' null expected the status to be NOT_DONE but PAST_DUE found";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                () -> item.updateDescription("New description")
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void checkUpdateItemDescriptionThrowsExceptionForItemWithStatusDone() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var completedAt = LocalDateTime.now().plusMinutes(1);
        var dueAt = LocalDateTime.now().plusMinutes(2);

        var item = MotherObject.createCompleteItem(description, createdAt, dueAt, completedAt);

        String exceptionMessage = "Item with 'id' null expected the status to be NOT_DONE but DONE found";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                () -> item.updateDescription("New description")
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void checkMarkingAnItemAsCompletedIsSuccessful() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createItem(description, createdAt, dueAt);

        item.markCompleted(LocalDateTime.now());

        Assertions.assertThat(item.getStatus()).isEqualTo(ItemStatus.DONE);
        Assertions.assertThat(item.getDoneAt()).isEqualToIgnoringNanos(LocalDateTime.now());
    }

    @Test
    void checkMarkingAnItemAsCompletedThrowsExceptionForItemWithStatusPastDue() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now().minusMinutes(1);
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createPastDueItem(description, createdAt, dueAt);

        String exceptionMessage = "Item with 'id' null has status PAST_DUE and cannot be modified";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                () -> item.markCompleted(LocalDateTime.now().plusMinutes(2))
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void checkReOpeningACompletedItemIsSuccessful() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var completedAt = LocalDateTime.now().plusMinutes(2);
        var dueAt = LocalDateTime.now().plusMinutes(5);

        var item = MotherObject.createCompleteItem(description, createdAt, dueAt, completedAt);

        Assertions.assertThat(item.getStatus()).isEqualTo(ItemStatus.DONE);
        Assertions.assertThat(item.getDoneAt()).isEqualTo(completedAt);

        item.reOpen(LocalDateTime.now());
        Assertions.assertThat(item.getStatus()).isEqualTo(ItemStatus.NOT_DONE);
        Assertions.assertThat(item.getDoneAt()).isNull();
    }

    @Test
    void checkReOpeningANotDoneItemIsSuccessful() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createItem(description, createdAt, dueAt);

        item.reOpen(LocalDateTime.now());
        Assertions.assertThat(item.getStatus()).isEqualTo(ItemStatus.NOT_DONE);
        Assertions.assertThat(item.getDoneAt()).isNull();
    }

    @Test
    void checkReOpeningAItemThrowsExceptionForItemWithStatusPastDue() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now().minusMinutes(1);
        var dueAt = LocalDateTime.now();

        var item = MotherObject.createPastDueItem(description, createdAt, dueAt);

        String exceptionMessage = "Item with 'id' null has status PAST_DUE and cannot be modified";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                () -> item.reOpen(LocalDateTime.now())
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void checkReOpeningAItemThrowsExceptionForItemWhenCreateDateIsBiggerThanDueDate() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var completedAt = createdAt.plusMinutes(1);
        var dueAt = createdAt.plusMinutes(5);
        var reOpenAt = dueAt.plusMinutes(1);

        var item = MotherObject.createCompleteItem(description, createdAt, dueAt, completedAt);

        String exceptionMessage = "Item with 'id' null is past the due date and cannot be modified";
        ItemWithDueDateInThePastException exception = assertThrows(
                ItemWithDueDateInThePastException.class,
                () -> item.reOpen(reOpenAt)
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void checkMarkAnItemAsPastDueIsSuccessful() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now();
        var dueAt = LocalDateTime.now().plusMinutes(1);

        var item = MotherObject.createItem(description, createdAt, dueAt);

        item.markPastDue();

        Assertions.assertThat(item.getStatus()).isEqualTo(ItemStatus.PAST_DUE);
        Assertions.assertThat(item.getDoneAt()).isNull();
    }

    @Test
    void checkMarkAnItemAsPastDueThrowsExceptionForItemWithStatusDone() {
        var description = "Test Description";
        var createdAt = LocalDateTime.now().minusMinutes(2);
        var completedAt = LocalDateTime.now().minusMinutes(1);
        var dueAt = LocalDateTime.now();

        var item = MotherObject.createCompleteItem(description, createdAt, dueAt, completedAt);

        String exceptionMessage = "Item with 'id' null expected the status to be NOT_DONE but DONE found";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                item::markPastDue
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }
}