package com.ss.challenge.todolist.domain.items;

import com.ss.challenge.todolist.TestUtils.MotherObject;
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
        var createdAt = LocalDateTime.now().minusMinutes(1);
        var dueAt = LocalDateTime.now();

        var item = MotherObject.createPastDueItem(description, createdAt, dueAt);

        String exceptionMessage = "Item with 'id' null has status PAST_DUE and cannot be modified";
        ItemInForbiddenStatusException exception = assertThrows(
                ItemInForbiddenStatusException.class,
                () -> item.updateDescription("New description")
        );


        Assertions.assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo(exceptionMessage);
    }

    @Test
    void markOpen() {
    }
}