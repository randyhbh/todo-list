package com.ss.challenge.todolist.usecases.items.reopen;

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
class ReOpenItemUseCaseIT {

    @Autowired
    private ItemRepository itemRepository;

    private ReOpenItemUseCase reOpenItemUseCase;

    @BeforeEach
    void setUp() {
        reOpenItemUseCase = new ReOpenItemUseCase(NOPLogger.NOP_LOGGER, itemRepository);
    }

    @Test
    public void checkReOpenItemThrowsExceptionWhenItemDoesNotExist() {
        var itemId = 2L;
        var reOpenItemCommand = ReOpenItemCommand.fromRequest(itemId);

        var exception = assertThrows(EntityNotFoundException.class, () -> reOpenItemUseCase.reOpen(reOpenItemCommand));

        Assertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("Unable to find com.ss.challenge.todolist.domain.items.Item with id 2");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_DONE_ITEM.sql")
    public void checkReOpenItemIsSuccessful() {
        var itemId = 2L;
        var reOpenItemCommand = ReOpenItemCommand.fromRequest(itemId);

        reOpenItemUseCase.reOpen(reOpenItemCommand);

        var itemFromDb = itemRepository.findById(itemId).orElseThrow();

        Assertions.assertThat(itemFromDb.getId()).isNotNull().isEqualTo(itemId);
        Assertions.assertThat(itemFromDb.getStatus()).isNotNull().isEqualTo(ItemStatus.NOT_DONE);
    }
}