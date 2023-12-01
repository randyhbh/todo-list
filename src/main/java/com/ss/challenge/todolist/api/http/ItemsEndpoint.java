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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

    @Operation(summary = "Create a new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid request body supplied", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody CreateItemRequest request) {
        return createItem.create(CreateItemCommand.fromRequest(request));
    }

    @Operation(summary = "Update item description by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid request body supplied", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
    })
    @PatchMapping("/{id}/update-description")
    @ResponseStatus(HttpStatus.OK)
    public void updateItemDescription(@PathVariable Long id, @Valid @RequestBody UpdateItemDescriptionRequest request) {
        updateItem.update(UpdateItemCommand.fromRequest(request, id));
    }

    @Operation(summary = "Update the item status to DONE by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Item not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
    })
    @PatchMapping("/{id}/complete-item")
    @ResponseStatus(HttpStatus.OK)
    public void complete(@PathVariable Long id) {
        completeItem.complete(CompleteItemCommand.fromRequest(id));
    }

    @Operation(summary = "Update the item status to NOT_DONE by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Item not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
    })
    @PatchMapping(value = "/{id}/re-open-item")
    @ResponseStatus(HttpStatus.OK)
    public void reOpen(@PathVariable Long id) {
        reOpenItem.reOpen(ReOpenItemCommand.fromRequest(id));
    }

    @Operation(summary = "Returns a list of items filtered by status")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponse> find(@RequestParam(required = false, value = "false") boolean includeAllStatuses) {
        return findItemUseCase.find(FindItemsCommand.fromRequest(includeAllStatuses));
    }

    @Operation(summary = "Return item details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Item not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse details(@PathVariable Long id) {
        return detailsItemsUseCase.details(DetailsItemsCommand.fromRequest(id));
    }
}
