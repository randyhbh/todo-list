package com.ss.challenge.todolist.usecases.items.details;

import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class DetailsItemsUseCaseTest {

    @Autowired
    private ItemRepository itemRepository;

    private DetailsItemsUseCase detailsItemsUseCase;

    @BeforeEach
    void setUp() {
        detailsItemsUseCase = new DetailsItemsUseCase(itemRepository);
    }

    @Test
    public void checkDetailsForItemThrowsExceptionWhenItemsDoNotExist() {
        var itemId = 1L;
        var detailsItemsCommand = DetailsItemsCommand.fromRequest(itemId);

        var exception = assertThrows(EntityNotFoundException.class, () -> detailsItemsUseCase.details(detailsItemsCommand));

        Assertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("Unable to find com.ss.challenge.todolist.domain.items.Item with id 1");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_NOT_DONE_ITEM.sql")
    public void checkDetailsForItemIsSuccessful() {
        var itemId = 1L;
        var description = "test description";
        var detailsItemsCommand = DetailsItemsCommand.fromRequest(itemId);

        var item = detailsItemsUseCase.details(detailsItemsCommand);

        Assertions.assertThat(item).isNotNull();
        Assertions.assertThat(item.id()).isNotNull().isEqualTo(itemId);
        Assertions.assertThat(item.description()).isNotNull().isEqualTo(description);
        Assertions.assertThat(item.status()).isNotNull().isEqualTo(ItemStatus.NOT_DONE);
        Assertions.assertThat(item.dueAt()).isNotNull();
    }
}