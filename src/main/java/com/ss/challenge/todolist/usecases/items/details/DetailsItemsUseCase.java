package com.ss.challenge.todolist.usecases.items.details;

import com.ss.challenge.todolist.api.http.mappers.ItemResponseMapper;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a use case responsible for getting details for an item given the id.
 */
@Service
public class DetailsItemsUseCase {

    private final Logger logger;
    private final ItemRepository repository;

    public DetailsItemsUseCase(Logger logger, ItemRepository repository) {
        this.logger = logger;
        this.repository = repository;
    }

    @Transactional
    public ItemResponse details(DetailsItemsCommand detailsItemsCommand) {
        Item item = repository.getReferenceById(detailsItemsCommand.getId());

        if (logger.isDebugEnabled()) {
            logger.info("Details for Item id: " + item.getId() + " were requested");
        }

        return ItemResponseMapper.toResponse(item);
    }
}
