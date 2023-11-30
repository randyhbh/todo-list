package com.ss.challenge.todolist.usecases.items.update;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a use case responsible for updating the description of a item.
 */
@Service
public class UpdateItemUseCase {

    private final ItemRepository repository;

    public UpdateItemUseCase(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void update(UpdateItemCommand itemCommand) {
        Item item = repository.getReferenceById(itemCommand.getItemId());
        repository.save(item.updateDescription(itemCommand.getDescription()));
    }
}
