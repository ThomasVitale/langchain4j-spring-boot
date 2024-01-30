# LangChain4J Spring Boot

[LangChain4J](https://github.com/langchain4j) support in Spring Boot to build AI and LLM-powered applications.

## 🚀&nbsp; Getting Started

### Prerequisites

* Java 17+
* Spring Boot 3+

## 🦜 Models

### OpenAI

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-openai-spring-boot-starter:0.4.0'
```

Configuration:

```yaml
langchain4j:
  open-ai:
    api-key: ${OPENAI_API_KEY}
```

Example:

```java
@RestController
class ChatController {
    private final ChatLanguageModel chatLanguageModel;

    ChatController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping("/ai/chat")
    String chat(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String message) {
        return chatLanguageModel.generate(message);
    }
}
```

### Ollama

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-ollama-spring-boot-starter:0.4.0'
testImplementation 'io.thomasvitale.langchain4j:langchain4j-spring-boot-testcontainers:0.4.0'
```

Configuration:

```yaml
langchain4j:
  ollama:
    chat:
      model: llama2
```

Example:

```java
@RestController
class ChatController {
    private final ChatLanguageModel chatLanguageModel;

    ChatController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping("/ai/chat")
    String chat(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String message) {
        return chatLanguageModel.generate(message);
    }
}
```

Testcontainers:

```java
@TestConfiguration(proxyBeanMethods = false)
public class TestChatModelsOllamaApplication {

    @Bean
    @RestartScope
    @ServiceConnection
    OllamaContainer ollama() {
        return new OllamaContainer("ghcr.io/thomasvitale/ollama-llama2");
    }

    public static void main(String[] args) {
        SpringApplication.from(ChatModelsOllamaApplication::main).with(TestChatModelsOllamaApplication.class).run(args);
    }
}
```

## 🌟 Examples

Check these [examples](https://github.com/ThomasVitale/llm-apps-java-langchain4j) to see LangChain4j and Spring Boot in action.

## 🛡️&nbsp; Security

The security process for reporting vulnerabilities is described in [SECURITY.md](SECURITY.md).

## 🖊️&nbsp; License

This project is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for more information.
