package com.ss.challenge.todolist.usecases.items.update;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a use case responsible for updating the description of a item.
 */
@Service
public class UpdateItemUseCase {

    private final Logger logger;
    private final ItemRepository repository;

    public UpdateItemUseCase(Logger logger, ItemRepository repository) {
        this.logger = logger;
        this.repository = repository;
    }

    @Transactional
    public void update(UpdateItemCommand itemCommand) {
        Item item = repository.getReferenceById(itemCommand.getItemId());

        repository.save(item.updateDescription(itemCommand.getDescription()));

        if (logger.isDebugEnabled()) {
            logger.info("Item id: " + item.getId() + " description was updated");
        }
    }
}
