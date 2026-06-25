package com.goel4607.chatclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@ActiveProfiles("openai")
@EnableWireMock(@ConfigureWireMock(baseUrlProperties = "openai.base.url"))
@SpringBootTest(properties = "spring.ai.openai.base-url=${openai.base.url}")
class SpringAiChatOpenaiServiceWireMockTest {
    @Value("classpath:/test-openai-response.json")
    Resource responseResource;

    @Autowired
    SpringAiChatService springAiChatService;

    @BeforeEach
    void setUp() throws IOException  {
        var cannedResponse = responseResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);
        WireMock.stubFor(WireMock.post("/chat/completions").willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

//    @Test
//    void askQuestion() {
//        var springAiChatService = new SpringAiChatService(this.chatClientBuilder);
//        var answer = springAiChatService.askQuestion(new Question("What is the capital of France?"));
//
//        Assertions.assertNotNull(answer);
//        Assertions.assertEquals("Paris", answer.answer());
//    }

    @Test
    void askQuestion() {
        var answer = springAiChatService.askQuestion(new Question("checkers","How many pieces are there?"));

        Assertions.assertNotNull(answer);
        Assertions.assertEquals("Paris", answer.answer());
    }
}