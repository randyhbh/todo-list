package com.ss.challenge.todolist.usecases.items.find;

import com.ss.challenge.todolist.api.http.mappers.ItemResponseMapper;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a use case responsible for find all items by status.
 */
@Service
public class FindItemsUseCase {

    private final ItemRepository repository;

    public FindItemsUseCase(ItemRepository repository) {
        this.repository = repository;
    }

    public List<ItemResponse> find(FindItemsCommand findItemsCommand) {
        try (Stream<Item> items = repository.streamByStatusIn(Arrays.asList(findItemsCommand.getStatuses()))){
            return items.map(ItemResponseMapper::toResponse).collect(Collectors.toList());
        }
    }
}
