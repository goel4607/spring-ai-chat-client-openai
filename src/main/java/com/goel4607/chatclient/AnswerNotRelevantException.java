package com.goel4607.chatclient;

public class AnswerNotRelevantException extends RuntimeException {
    public AnswerNotRelevantException(String question, String answer) {
        super(String.format("Answer %s is not relevant to the question %s", answer, question));
    }
}
