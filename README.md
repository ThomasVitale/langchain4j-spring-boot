# LangChain4j Spring Boot

This project brings [LangChain4j](https://github.com/langchain4j) support in Spring Boot
to build AI and LLM-powered applications. It provides integrations with LLM services
and vector stores, as well as tools, chains, and AI services.

Using the starter projects in this repository, you gain the following advantages over
using the vanilla LangChain4j libraries in Spring Boot:

* Autoconfiguration and unified configuration properties for models and vector stores
* HTTP infrastructure with RestClient, WebClient, and Jackson for all integrations
* Built-in observability with Micrometer, including LLM-specific metrics and traces
* Dev services with Docker Compose and Testcontainers for models and vector stores.

## 🚀&nbsp; Quick Start

### Pre-Requisites

* Java 17+
* Docker/Podman
* [Spring CLI](https://docs.spring.io/spring-cli/reference/installation.html)

### Getting Started

Using the Spring CLI, you can easily bootstrap a new Spring Boot application with LangChain4j support.

First, add the LangChain4j Spring Boot catalog providing the project templates.

```shell
spring project-catalog add langchain4j https://github.com/ThomasVitale/langchain4j-spring-boot
```

Then, create a new Spring Boot project for building an LLM Applications with LangChain4j and Ollama.

```shell
spring boot new myapp langchain4j-chat-ollama
```

Finally, navigate to the `myapp` folder and run the Spring Boot application. The first time you run it,
it will take a while to download the Ollama container image used as a dev service based on the Testcontainers
Spring Boot integration.

```shell
cd myapp
./mvnw spring-boot:run
```

You can now call the application that will use Ollama and _llama2_ to generate a text response.
This example uses [httpie](https://httpie.io) to send HTTP requests.

```shell
http :8080/ai/chat message=="What is the capital of Italy?"
```

## 🦜 Models

### OpenAI

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-openai-spring-boot-starter:0.6.0'
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
implementation 'io.thomasvitale.langchain4j:langchain4j-ollama-spring-boot-starter:0.6.0'
testImplementation 'io.thomasvitale.langchain4j:langchain4j-spring-boot-testcontainers:0.6.0'
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
