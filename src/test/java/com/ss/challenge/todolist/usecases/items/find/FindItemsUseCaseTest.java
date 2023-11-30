package com.ss.challenge.todolist.usecases.items.find;

import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class FindItemsUseCaseTest {

    @Autowired
    private ItemRepository itemRepository;

    private FindItemsUseCase findItemsUseCase;

    @BeforeEach
    void setUp() {
        findItemsUseCase = new FindItemsUseCase(itemRepository);
    }

    @Test
    public void checkFindItemsReturnsEmptyWhenItemsDoNotExist() {
        var reOpenItemCommand = FindItemsCommand.fromRequest(false);
        var items = findItemsUseCase.find(reOpenItemCommand);
        Assertions.assertThat(items).isNotNull().isEmpty();
    }

    @Test
    @Sql("classpath:scripts/INIT_FIVE_ITEMS.sql")
    public void checkFindItemsReturnsAllItemsWhitStatusNotDone() {
        var reOpenItemCommand = FindItemsCommand.fromRequest(false);
        var items = findItemsUseCase.find(reOpenItemCommand);
        Assertions.assertThat(items).isNotNull().hasSize(3);
    }

    @Test
    @Sql("classpath:scripts/INIT_FIVE_ITEMS.sql")
    public void checkFindItemsReturnsAllItemsInAnyStatus() {
        var reOpenItemCommand = FindItemsCommand.fromRequest(true);
        var items = findItemsUseCase.find(reOpenItemCommand);
        Assertions.assertThat(items).isNotNull().hasSize(5);
    }
}