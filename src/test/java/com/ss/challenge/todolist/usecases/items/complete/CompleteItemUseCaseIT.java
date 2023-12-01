package com.ss.challenge.todolist.usecases.items.complete;

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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class CompleteItemUseCaseIT {

    @Autowired
    private ItemRepository itemRepository;

    private CompleteItemUseCase completeItemUseCase;

    @BeforeEach
    void setUp() {
        completeItemUseCase = new CompleteItemUseCase(NOPLogger.NOP_LOGGER, itemRepository);
    }

    @Test
    public void checkCompleteItemThrowsExceptionWhenItemDoesNotExist() {
        var itemId = 1L;
        var updateItemCommand = CompleteItemCommand.fromRequest(itemId);

        var exception = assertThrows(EntityNotFoundException.class, () -> completeItemUseCase.complete(updateItemCommand));

        Assertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("Unable to find com.ss.challenge.todolist.domain.items.Item with id 1");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_NOT_DONE_ITEM.sql")
    public void checkCompleteItemIsSuccessful() {
        var itemId = 1L;
        var updateItemCommand = CompleteItemCommand.fromRequest(itemId);

        completeItemUseCase.complete(updateItemCommand);

        var itemFromDb = itemRepository.findById(itemId).orElseThrow();

        Assertions.assertThat(itemFromDb.getId()).isNotNull().isEqualTo(itemId);
        Assertions.assertThat(itemFromDb.getStatus()).isNotNull().isEqualTo(ItemStatus.DONE);
        Assertions.assertThat(itemFromDb.getDoneAt()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
    }
}