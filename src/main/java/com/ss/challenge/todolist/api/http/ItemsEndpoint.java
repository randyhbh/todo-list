package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import com.ss.challenge.todolist.api.http.requests.UpdateItemDescriptionRequest;
import com.ss.challenge.todolist.api.http.responses.ItemResponse;
import com.ss.challenge.todolist.usecases.items.complete.CompleteItemCommand;
import com.ss.challenge.todolist.usecases.items.complete.CompleteItemUseCase;
import com.ss.challenge.todolist.usecases.items.create.CreateItemCommand;
import com.ss.challenge.todolist.usecases.items.create.CreateItemUseCase;
import com.ss.challenge.todolist.usecases.items.details.DetailsItemsCommand;
import com.ss.challenge.todolist.usecases.items.details.DetailsItemsUseCase;
import com.ss.challenge.todolist.usecases.items.find.FindItemsCommand;
import com.ss.challenge.todolist.usecases.items.find.FindItemsUseCase;
import com.ss.challenge.todolist.usecases.items.reopen.ReOpenItemCommand;
import com.ss.challenge.todolist.usecases.items.reopen.ReOpenItemUseCase;
import com.ss.challenge.todolist.usecases.items.update.UpdateItemCommand;
import com.ss.challenge.todolist.usecases.items.update.UpdateItemUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemsEndpoint {

    private final CreateItemUseCase createItem;
    private final UpdateItemUseCase updateItem;
    private final CompleteItemUseCase completeItem;
    private final ReOpenItemUseCase reOpenItem;
    private final FindItemsUseCase findItemUseCase;
    private final DetailsItemsUseCase detailsItemsUseCase;

    public ItemsEndpoint(
            CreateItemUseCase createItemUseCase,
            UpdateItemUseCase updateItemUseCase,
            CompleteItemUseCase completeItemUseCase,
            ReOpenItemUseCase reOpenItemUseCase,
            FindItemsUseCase findItemUseCase,
            DetailsItemsUseCase detailsItemsUseCase
    ) {
        this.createItem = createItemUseCase;
        this.updateItem = updateItemUseCase;
        this.completeItem = completeItemUseCase;
        this.reOpenItem = reOpenItemUseCase;
        this.findItemUseCase = findItemUseCase;
        this.detailsItemsUseCase = detailsItemsUseCase;
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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponse> find(@RequestParam(required = false, value = "false") boolean includeAllStatuses) {
        return findItemUseCase.find(FindItemsCommand.fromRequest(includeAllStatuses));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse details(@PathVariable Long id) {
        return detailsItemsUseCase.details(DetailsItemsCommand.fromRequest(id));
    }
}
