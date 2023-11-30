package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import org.springframework.web.bind.annotation.RestController;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.usecases.items.reopen.ReOpenItemCommand;
import com.ss.challenge.todolist.usecases.items.reopen.ReOpenItemUseCase;

@RestController
@RequestMapping("/items")
public class ItemsEndpoint {

    private final CreateItemUseCase createItem;
    private final UpdateItemUseCase updateItem;
    private final CompleteItemUseCase completeItem;
    private final ReOpenItemUseCase reOpenItem;

    public ItemsEndpoint(
            CreateItemUseCase createItemUseCase,
            UpdateItemUseCase updateItemUseCase,
            CompleteItemUseCase completeItemUseCase,
            ReOpenItemUseCase reOpenItemUseCase,
    ) {
        this.createItem = createItemUseCase;
        this.updateItem = updateItemUseCase;
        this.completeItem = completeItemUseCase;
        this.reOpenItem = reOpenItemUseCase;
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

    @PatchMapping("/{id}/complete-item")
    @ResponseStatus(HttpStatus.OK)
    public void complete(@PathVariable Long id) {
        completeItem.complete(CompleteItemCommand.fromRequest(id));
    }

    @PatchMapping(value = "/{id}/re-open-item")
    @ResponseStatus(HttpStatus.OK)
    public void reOpen(@PathVariable Long id) {
        reOpenItem.reOpen(ReOpenItemCommand.fromRequest(id));
    }

}
