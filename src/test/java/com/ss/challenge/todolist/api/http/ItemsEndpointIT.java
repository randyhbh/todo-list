package com.ss.challenge.todolist.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.challenge.todolist.api.http.requests.CreateItemRequest;
import com.ss.challenge.todolist.api.http.requests.UpdateItemDescriptionRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

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

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("creating an item with empty description fails with bad request")
    public void testCreateItemValidationFailed() throws Exception {
        var data = new CreateItemRequest("", LocalDateTime.now().plusMinutes(1));
        MvcResult mvcResult = this.mockMvc.perform(
                        post("/items")
                                .content(objectMapper.writeValueAsBytes(data))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        var body = objectMapper.readValue(contentAsString, ProblemDetail.class);

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getProperties()).isNotNull();
        Assertions.assertThat(body.getProperties().size()).isEqualTo(1);
        Assertions.assertThat(body.getProperties().get("description")).isEqualTo("must not be empty");
    }

    @Test
    @DisplayName("updating an item with an empty description fails with bad request")
    public void testUpdateItemDescriptionValidationFailed() throws Exception {
        var data = new UpdateItemDescriptionRequest("");
        MvcResult mvcResult = this.mockMvc.perform(
                        patch("/items/1/update-description")
                                .content(objectMapper.writeValueAsBytes(data))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        var body = objectMapper.readValue(contentAsString, ProblemDetail.class);

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getProperties()).isNotNull();
        Assertions.assertThat(body.getProperties().size()).isEqualTo(1);
        Assertions.assertThat(body.getProperties().get("description")).isEqualTo("must not be empty");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_DONE_ITEM.sql")
    @DisplayName("update description for an item with status done fails with unprocessable entity")
    public void testUpdateItemDescriptionThrowsItemInForbiddenStatusException() throws Exception {
        var itemId = 1;
        var data = new UpdateItemDescriptionRequest("a description");

        MvcResult mvcResult = this.mockMvc.perform(
                        patch("/items/" + itemId + "/update-description")
                                .content(objectMapper.writeValueAsBytes(data))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        var body = objectMapper.readValue(contentAsString, ProblemDetail.class);

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getDetail()).isEqualTo("Item with 'id' 1 expected the status to be NOT_DONE but DONE found");
    }

    @Test
    @Sql("classpath:scripts/INIT_ONE_DONE_ITEM_WITH_PAST_DUE_DATE.sql")
    @DisplayName("re-opening an item with status done after his due date fails with unprocessable entity")
    public void testReOpenItemThrowsItemItemWithDueDateInThePastException() throws Exception {
        var itemId = 2;

        MvcResult mvcResult = this.mockMvc.perform(
                        patch("/items/" + itemId + "/re-open-item")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        var body = objectMapper.readValue(contentAsString, ProblemDetail.class);

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getDetail()).isEqualTo("Item with 'id' 2 is past the due date and cannot be modified");
    }
}