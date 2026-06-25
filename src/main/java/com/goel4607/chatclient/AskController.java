package com.goel4607.chatclient;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {
    private final ChatService service;

    public AskController(@Qualifier("springAiChatService") ChatService service) {
        this.service = service;
    }

    @PostMapping(path = "/ask", produces = "application/json")
    public Answer askQuestion(@RequestBody @Valid Question question) {
        return this.service.askQuestion(question);
    }
}
