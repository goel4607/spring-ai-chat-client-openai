# Spring AI Chat Client

A Spring Boot application demonstrating how to use Spring AI with multiple AI providers (OpenAI and Ollama), switchable via Spring profiles.

## Features

- Chat endpoint (`POST /ask`) backed by Spring AI `ChatClient`
- Profile-based provider switching — no code changes needed to swap providers
- WireMock-based unit tests for both providers (no real AI calls)
- Evaluation tests (relevancy + fact-checking) using Spring AI evaluators

## Providers

| Profile | Provider | Model |
|---|---|---|
| `openai` | OpenAI | gpt-5-mini |
| `ollama` | Ollama (local) | gemma3:4b |

## Switching Providers

In `src/main/resources/application.properties`:

```properties
spring.profiles.active=openai   # or ollama
```

## Running

**Prerequisites:**
- OpenAI API key set as `OPENAI_API_KEY` env var (for `openai` profile)
- [Ollama](https://ollama.com) installed and running (for `ollama` profile)

```bash
./mvnw spring-boot:run
```

**Test the endpoint:**

```bash
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question":"why is the sky blue?"}'
```

## Testing

**Unit tests** (WireMock, no real AI):
```bash
./mvnw test
```

**Integration/eval tests** (requires Ollama running):
```bash
./mvnw verify
```

## Actuator

Beans endpoint is exposed at:
```
GET http://localhost:8080/actuator/beans
```
