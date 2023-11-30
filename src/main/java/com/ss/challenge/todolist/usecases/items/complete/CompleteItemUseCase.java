package com.ss.challenge.todolist.usecases.items.complete;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * This class is a use case responsible for marking an item as completed.
 */
@Service
public class CompleteItemUseCase {

    private final Logger logger;
    private final ItemRepository itemRepository;

    public CompleteItemUseCase(Logger logger, ItemRepository itemRepository) {
        this.logger = logger;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void complete(CompleteItemCommand itemCommand) {
        Item item = itemRepository.getReferenceById(itemCommand.getItemId());

        itemRepository.save(item.markCompleted(LocalDateTime.now()));

        if (logger.isDebugEnabled()) {
            logger.info("Item id: " + item.getId() + " was marked as completed");
        }
    }
}
