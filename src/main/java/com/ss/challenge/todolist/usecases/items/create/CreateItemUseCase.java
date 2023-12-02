package com.ss.challenge.todolist.usecases.items.create;

import com.ss.challenge.todolist.api.http.mappers.ItemResponseMapper;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * This class is a use case responsible for creating a new item.
 */
@Service
public class CreateItemUseCase {

    private final Logger logger;
    private final ItemRepository repository;

    public CreateItemUseCase(Logger logger, ItemRepository repository) {
        this.logger = logger;
        this.repository = repository;
    }

    @Transactional
    public ItemResponse create(CreateItemCommand itemCommand) {
        var item = Item.createItem(
                itemCommand.getDescription(),
                LocalDateTime.now(),
                itemCommand.getDueAt()
        );
        repository.save(item);

        if (logger.isDebugEnabled()) {
            logger.info("New " + item + " created");
        }

        return ItemResponseMapper.toResponse(item);
    }
}
