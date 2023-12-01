package com.ss.challenge.todolist.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import com.ss.challenge.todolist.api.http.requests.UpdateItemDescriptionRequest;
import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import com.ss.challenge.todolist.domain.items.exceptions.ItemWithDueDateInThePastException;
import com.ss.challenge.todolist.infra.persistence.h2.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemsEndpointIT {

    @Autowired
    ItemsEndpoint itemsEndpoint;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemRepository itemRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateItemValidationFailed() throws Exception {
        when(this.itemRepository.save(any(Item.class))).thenReturn(new Item());

        var data = new CreateItemRequest("", LocalDateTime.now().plusMinutes(1));
        this.mockMvc.perform(
                post("/items")
                        .content(objectMapper.writeValueAsBytes(data))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verify(this.itemRepository, times(0)).save(any(Item.class));
        verifyNoMoreInteractions(this.itemRepository);
    }

    @Test
    public void testUpdateItemDescriptionValidationFailed() throws Exception {
        when(this.itemRepository.save(any(Item.class))).thenReturn(new Item());

        var data = new UpdateItemDescriptionRequest("");
        this.mockMvc.perform(
                patch("/items/1/update-description")
                        .content(objectMapper.writeValueAsBytes(data))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verify(this.itemRepository, times(0)).save(any(Item.class));
        verifyNoMoreInteractions(this.itemRepository);
    }

    @Test
    public void testUpdateItemDescriptionThrowsItemInForbiddenStatusException() throws Exception {
        Item item = mock();
        when(item.updateDescription(anyString())).thenThrow(new ItemInForbiddenStatusException("Item with 'id' null has status " + ItemStatus.PAST_DUE + " and cannot be modified"));

        when(this.itemRepository.getReferenceById(anyLong())).thenReturn(item);

        var data = new UpdateItemDescriptionRequest("a description");
        this.mockMvc.perform(
                patch("/items/1/update-description")
                        .content(objectMapper.writeValueAsBytes(data))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());

        verify(this.itemRepository, times(1)).getReferenceById(anyLong());
        verifyNoMoreInteractions(this.itemRepository);
    }

    @Test
    public void testReOpenItemThrowsItemItemWithDueDateInThePastException() throws Exception {
        Item item = mock();
        when(item.reOpen(any(LocalDateTime.class))).thenThrow(new ItemWithDueDateInThePastException("Item with 'id' null is past the due date and cannot be modified"));

        when(this.itemRepository.getReferenceById(anyLong())).thenReturn(item);

        var data = new UpdateItemDescriptionRequest("a description");
        this.mockMvc.perform(
                patch("/items/1/re-open-item")
                        .content(objectMapper.writeValueAsBytes(data))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());

        verify(this.itemRepository, times(1)).getReferenceById(anyLong());
        verifyNoMoreInteractions(this.itemRepository);
    }

}