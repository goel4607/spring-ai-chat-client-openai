package com.goel4607.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SpringAiChatService implements ChatService {
    private final ChatClient chatClient;
    private final GameRulesService rulesService;

    public SpringAiChatService(ChatClient.Builder chatClientBuilder, GameRulesService rulesService) {
        this.chatClient = chatClientBuilder.build();
        this.rulesService = rulesService;
    }

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource promptTemplate;

    @Override
    public Answer askQuestion(Question question) {
        final var answer =
                this.chatClient
                        .prompt()
                        .system(spec -> spec
                                .text(this.promptTemplate)
                                .param("gameTitle", question.gameTitle())
                                .param("rules", this.rulesService.getRulesFor(question.gameTitle())))
                        .user(question.question())
                        .call()
                        .content();
        return new Answer(question.gameTitle(), answer);
    }
}
