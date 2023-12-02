package com.ss.challenge.todolist.usecases.expiration.apply;

import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class ApplyExpirationUseCaseIT {

    @Autowired
    private ItemRepository itemRepository;

    private ApplyExpirationUseCase applyExpirationUseCase;

    @BeforeEach
    void setUp() {
        applyExpirationUseCase = new ApplyExpirationUseCase(NOPLogger.NOP_LOGGER, itemRepository);
    }

    @Test
    @Sql("classpath:scripts/INIT_FIVE_ITEMS_TO_EXPIRE.sql")
    public void checkMarkItemAsDueWithUpdateQueryIsSuccessful() {

        var items = itemRepository.findAll();
        Assertions.assertThat(items.size()).isEqualTo(5);
        Assertions.assertThat(items.stream().filter(item -> item.getStatus() == ItemStatus.PAST_DUE).toList().size()).isEqualTo(1);

        applyExpirationUseCase.setExpirationDueToItemsWithUpdateQuery();

        items = itemRepository.findAll();
        Assertions.assertThat(items.size()).isEqualTo(5);
        Assertions.assertThat(items.stream().filter(item -> item.getStatus() == ItemStatus.PAST_DUE).toList().size()).isEqualTo(4);
    }

}