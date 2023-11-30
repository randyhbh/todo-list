package com.ss.challenge.todolist.usecases.items.details;

import com.ss.challenge.todolist.api.http.mappers.ItemResponseMapper;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a use case responsible for creating a new item for the todo-list.
 */
@Service
public class DetailsItemsUseCase {

    private final ItemRepository repository;

    public DetailsItemsUseCase(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ItemResponse details(DetailsItemsCommand detailsItemsCommand) {
        Item item = repository.getReferenceById(detailsItemsCommand.getId());
        return ItemResponseMapper.toResponse(item);
    }
}
