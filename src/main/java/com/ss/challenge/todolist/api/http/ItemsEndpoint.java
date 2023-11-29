package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import org.springframework.web.bind.annotation.RestController;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;

@RestController
@RequestMapping("/items")
public class ItemsEndpoint {

    private final CreateItemUseCase createItem;
    public ItemsEndpoint(
            CreateItemUseCase createItemUseCase,
    ) {
        this.createItem = createItemUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody CreateItemRequest request) {
        return createItem.create(CreateItemCommand.fromRequest(request));
    }

}
