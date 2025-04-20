# LangChain4j Spring Boot

> **Warning**
> This package has been archived in favour of the upstream [LangChain4j Spring](https://github.com/langchain4j/langchain4j-spring) project.

This project brings [LangChain4j](https://github.com/langchain4j) support in Spring Boot
to build AI and LLM-powered applications. It provides integrations with LLM services
and vector stores, as well as tools, chains, and AI services.

Using the starter projects in this repository, you gain the following advantages over
using the vanilla LangChain4j libraries in Spring Boot:

* Autoconfiguration and unified configuration properties for models and vector stores
* HTTP infrastructure with RestClient, WebClient, and Jackson for all integrations
* Built-in observability with Micrometer, including LLM-specific metrics and traces
* Dev services with Docker Compose and Testcontainers for models and vector stores
* Service bindings for automatic connection configuration when running on Kubernetes.

## 🦜 Models

### OpenAI

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-openai-spring-boot-starter:0.9.0'
```

Configuration:

```yaml
langchain4j:
  open-ai:
    client:
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
implementation 'io.thomasvitale.langchain4j:langchain4j-ollama-spring-boot-starter:0.9.0'
```

Configuration:

```yaml
langchain4j:
  ollama:
    chat:
      model: llama3
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

## 🫙 Vector Stores

### Chroma

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-chroma-spring-boot-starter:0.9.0'
```

Example:

```java
class ChromaDataIngestor {
    private final ChromaEmbeddingStore embeddingStore;
    private final EmbeddingModel embeddingModel;

    ChatController(ChromaEmbeddingStore embeddingStore, EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    public void ingest(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(300, 0))
                .build();
        ingestor.ingest(documents);
    }
}
```

### Weaviate

Gradle:

```groovy
implementation 'io.thomasvitale.langchain4j:langchain4j-weaviate-spring-boot-starter:0.9.0'
```

Example:

```java
class WeaviateDataIngestor {
    private final WeaviateEmbeddingStore embeddingStore;
    private final EmbeddingModel embeddingModel;

    ChatController(WeaviateEmbeddingStore embeddingStore, EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    public void ingest(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(300, 0))
                .build();
        ingestor.ingest(documents);
    }
}
```

## 🌟 Examples

Check these [examples](https://github.com/ThomasVitale/llm-apps-java-langchain4j) to see LangChain4j and Spring Boot in action.

## 🛡️&nbsp; Security

The security process for reporting vulnerabilities is described in [SECURITY.md](SECURITY.md).

## 🖊️&nbsp; License

This project is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for more information.
