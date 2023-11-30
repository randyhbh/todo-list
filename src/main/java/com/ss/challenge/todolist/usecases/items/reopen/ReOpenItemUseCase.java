package com.ss.challenge.todolist.usecases.items.reopen;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * This class is a use case responsible for re-open an item that was completed.
 */
@Service
public class ReOpenItemUseCase {

    private final Logger logger;
    private final ItemRepository itemRepository;

    public ReOpenItemUseCase(Logger logger, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.logger = logger;
    }

    @Transactional
    public void reOpen(ReOpenItemCommand itemCommand) {
        Item item = itemRepository.getReferenceById(itemCommand.getItemId());
        itemRepository.save(item.reOpen(LocalDateTime.now()));
        if (logger.isDebugEnabled()) {
            logger.info("Item id: " + item.getId() + " was reopened");
        }
    }
}
