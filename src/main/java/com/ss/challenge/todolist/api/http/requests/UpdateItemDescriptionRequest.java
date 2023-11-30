package com.ss.challenge.todolist.api.http.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public record UpdateItemDescriptionRequest(
        @NotNull
        @NotEmpty
        String description
) {
}
