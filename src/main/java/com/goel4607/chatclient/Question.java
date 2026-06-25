package com.goel4607.chatclient;

import jakarta.validation.constraints.NotBlank;

public record Question(@NotBlank (message = "Game title is required") String gameTitle, @NotBlank (message = "Question is required") String question) {
}
