package com.goel4607.chatclient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

@Tag("integration")
@ActiveProfiles("ollama")
@SpringBootTest
public class SpringAiChatServiceEvalTests {
    String userText = "Why is the sky blue?";

    @Autowired
    @Qualifier("springAiChatService")
    private ChatService chatService;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    private RelevancyEvaluator relevancyEvaluator;
    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    public void setup(){
        this.relevancyEvaluator = new RelevancyEvaluator(this.chatClientBuilder);
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(this.chatClientBuilder).build();
    }

    @Test
    public void evaluateRelevancy() {

        Question question = new Question("Science", userText);
        Answer answer = this.chatService.askQuestion(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        System.out.println("evaluationResponse: " + evaluationResponse);

        Assertions.assertThat(evaluationResponse.isPass()).withFailMessage("""
                ===================================
                The answer "%s"
                is not considered relevant for the question
                "%s"
                ===================================
                """, answer.answer(), userText).isTrue();
    }

    @Test
    public void evaluateFactualAccuracy() {
        var question = new Question("Science", userText);
        var answer = this.chatService.askQuestion(question);

        var evalRequest = new EvaluationRequest(userText, answer.answer());
        var evalResponse = this.factCheckingEvaluator.evaluate(evalRequest);

        Assertions.assertThat(evalResponse.isPass()).withFailMessage("""
                ==================================
                The answer "%s"
                is not considered correct for the question
                "%s"
                ==================================
                """, answer.answer(), userText).isTrue();
    }
}
