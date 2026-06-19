package com.goel4607.chatclientopenai;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {
    private final ChatService service;

    public AskController(SpringAiChatService service) {
        this.service = service;
    }

    @PostMapping(path = "/ask", produces = "application/json")
    public Answer askQuestion(@RequestBody Question question) {
        return this.service.askQuestion(question);
    }
}
