package com.goel4607.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class SelfEvaluationChatService implements ChatService{
    private final ChatClient chatClient;
    private final RelevancyEvaluator relevancyEvaluator;

    public SelfEvaluationChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Override
    @Retryable(retryFor = AnswerNotRelevantException.class, maxAttempts = 5)
    public Answer askQuestion(Question question) {
        var answer = this.chatClient.prompt().user(question.question()).call().content();
        evaluateRelevancy(question.question(), answer);
        return new Answer(answer);
    }

    @Recover
    public Answer recover(AnswerNotRelevantException e) {
        return new Answer("I'm sorry, I wasn't able to answer the question.");
    }

    private void evaluateRelevancy(String question, String answer) {
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, answer);
        EvaluationResponse evaluationResponse = this.relevancyEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            System.out.println("Failed evaluation!!");
            throw new AnswerNotRelevantException(question, answer);
        }
    }
}
