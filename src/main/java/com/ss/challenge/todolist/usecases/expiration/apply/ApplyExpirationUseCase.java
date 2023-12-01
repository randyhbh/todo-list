package com.ss.challenge.todolist.usecases.expiration.apply;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ApplyExpirationUseCase {

    private final Logger logger;
    private final ItemRepository repository;

    public ApplyExpirationUseCase(Logger logger, ItemRepository repository) {
        this.logger = logger;
        this.repository = repository;
    }

    @Transactional
    @Scheduled(cron = "${scheduler.tasks.item-past-due.interval}")
    public void setExpirationDueToItemsWithUpdateQuery() {
        var updatedItems = repository.setPastDueOnExpiredItems();
        if (logger.isDebugEnabled()) {
            logger.info(updatedItems + "->Items where set to " + ItemStatus.PAST_DUE);
        }
    }
}
