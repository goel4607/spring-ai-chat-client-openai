package com.goel4607.chatclientopenai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiChatService implements ChatService {
    private final ChatClient chatClient;

    public SpringAiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answer =
                this.chatClient
                        .prompt()
                        .user(question.question())
                        .call()
                        .content();
        return new Answer(answer);
    }
}
