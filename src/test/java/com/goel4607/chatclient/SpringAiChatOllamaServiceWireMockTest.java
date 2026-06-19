package com.goel4607.chatclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@ActiveProfiles("ollama")
@EnableWireMock (@ConfigureWireMock(baseUrlProperties = "ollama.base.url"))
@SpringBootTest (properties = "spring.ai.ollama.base-url=${ollama.base.url}")
public class SpringAiChatOllamaServiceWireMockTest {
    @Value("classpath:/test-ollama-response.json")
    Resource responseResource;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @BeforeEach
    public void setup() throws IOException  {
        var cannedResponse = responseResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);

        WireMock.stubFor(WireMock.post("/api/chat").willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

    @Test
    public void askQuestion() {
        var springAiService = new SpringAiChatService(this.chatClientBuilder);
        var answer = springAiService.askQuestion(new Question("What is the capital of France?"));
        Assertions.assertNotNull(answer);
        Assertions.assertEquals("Paris", answer.answer());
    }
}
