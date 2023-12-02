package com.ss.challenge.todolist.usecases.items.update;

import com.ss.challenge.todolist.api.http.requests.UpdateItemDescriptionRequest;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UpdateItemUseCaseIT {

    @Autowired
    private ItemRepository itemRepository;

    private UpdateItemUseCase updateItemUseCase;

    @BeforeEach
    void setUp() {
        updateItemUseCase = new UpdateItemUseCase(NOPLogger.NOP_LOGGER, itemRepository);
    }

    @Test
    public void checkUpdateItemThrowsExceptionWhenItemDoesNotExist() {
        var itemId = 1L;
        var description = "A new description";
        var updateItemRequest = new UpdateItemDescriptionRequest(description);
        var updateItemCommand = UpdateItemCommand.fromRequest(updateItemRequest, itemId);

        var exception = assertThrows(EntityNotFoundException.class, () -> updateItemUseCase.update(updateItemCommand));

        Assertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("Unable to find com.ss.challenge.todolist.domain.items.Item with id 1");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_NOT_DONE_ITEM.sql")
    public void checkUpdateItemIsSuccessful() {
        var itemId = 1L;
        var description = "A new description";
        var updateItemRequest = new UpdateItemDescriptionRequest(description);
        var updateItemCommand = UpdateItemCommand.fromRequest(updateItemRequest, itemId);

        updateItemUseCase.update(updateItemCommand);

        var itemFromDb = itemRepository.findById(itemId).orElseThrow();

        Assertions.assertThat(itemFromDb.getId()).isNotNull().isEqualTo(itemId);
        Assertions.assertThat(itemFromDb.getDescription()).isNotNull().isEqualTo(description);
        Assertions.assertThat(itemFromDb.getStatus()).isNotNull().isEqualTo(ItemStatus.NOT_DONE);
    }
}