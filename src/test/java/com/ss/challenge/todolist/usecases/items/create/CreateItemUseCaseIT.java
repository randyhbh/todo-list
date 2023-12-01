package com.ss.challenge.todolist.usecases.items.create;

import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class CreateItemUseCaseIT {

    @Autowired
    private ItemRepository itemRepository;

    private CreateItemUseCase createItemUseCase;

    @BeforeEach
    void setUp() {
        createItemUseCase = new CreateItemUseCase(itemRepository);
    }

    @Test
    public void checkCreateItemIsSuccessful() {
        String description = "A description";
        LocalDateTime dueAt = LocalDateTime.now().plusMinutes(5);
        var createItemRequest = new CreateItemRequest(description, dueAt);
        var createItemCommand = CreateItemCommand.fromRequest(createItemRequest);

        var itemResponse = createItemUseCase.create(createItemCommand);

        Assertions.assertThat(itemResponse.id()).isNotNull();
        Assertions.assertThat(itemResponse.description()).isNotNull().isEqualTo(description);
        Assertions.assertThat(itemResponse.dueAt()).isNotNull().isEqualTo(dueAt);
        Assertions.assertThat(itemResponse.status()).isNotNull().isEqualTo(ItemStatus.NOT_DONE);

        var itemFromDb = itemRepository.findById(itemResponse.id()).orElseThrow();

        Assertions.assertThat(itemFromDb.getDoneAt()).isNull();
        Assertions.assertThat(itemFromDb.getCreatedAt()).isNotNull().isBefore(dueAt);
    }

    @Test
    public void checkCreateItemThrowsErrorWhenDescriptionIsEmpty() {
        var createItemRequest = new CreateItemRequest(null, LocalDateTime.now().plusMinutes(5));
        var createItemCommand = CreateItemCommand.fromRequest(createItemRequest);

        var exception = assertThrows(IllegalArgumentException.class, () -> createItemUseCase.create(createItemCommand));

        Assertions.assertThat(exception.getMessage()).isNotNull().isEqualTo("Item description cannot be NULL");
    }
}