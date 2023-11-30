package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import org.springframework.web.bind.annotation.RestController;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;

@RestController
@RequestMapping("/items")
public class ItemsEndpoint {

    private final CreateItemUseCase createItem;
    private final UpdateItemUseCase updateItem;
    public ItemsEndpoint(
            CreateItemUseCase createItemUseCase,
            UpdateItemUseCase updateItemUseCase,
    ) {
        this.createItem = createItemUseCase;
        this.updateItem = updateItemUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody CreateItemRequest request) {
        return createItem.create(CreateItemCommand.fromRequest(request));
    }

    @PatchMapping("/{id}/update-description")
    @ResponseStatus(HttpStatus.OK)
    public void updateItemDescription(@PathVariable Long id, @Valid @RequestBody UpdateItemDescriptionRequest request) {
        updateItem.update(UpdateItemCommand.fromRequest(request, id));
    }
}
