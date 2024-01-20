package io.thomasvitale.langchain4j.autoconfigure.ollama;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
@Testcontainers
class OllamaAutoConfigurationIT {

    private static final Logger log = LoggerFactory.getLogger(OllamaAutoConfigurationIT.class);
    private static final String MODEL_NAME = "orca-mini";

    @Container
    static GenericContainer<?> ollama = new GenericContainer<>("ghcr.io/thomasvitale/ollama/%s".formatted(MODEL_NAME))
            .withExposedPorts(11434);

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434));
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withPropertyValues("langchain4j.ollama.chat.model=%s".formatted(MODEL_NAME))
            .withPropertyValues("langchain4j.ollama.embedding.model=%s".formatted(MODEL_NAME))
            .withPropertyValues("langchain4j.ollama.chat.max-tokens=20")
            .withPropertyValues("langchain4j.ollama.timeout=60s")
            .withConfiguration(AutoConfigurations.of(OllamaAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner
                .withPropertyValues("langchain4j.ollama.baseUrl=%s".formatted(getBaseUrl()))
                .run(context -> {
                    OllamaChatModel model = context.getBean(OllamaChatModel.class);
                    String response = model.generate("What is the capital of Italy?");
                    log.info("Response: " + response);
                    assertThat(response).containsIgnoringCase("Rome");
                });
    }

    @Test
    void chatStreaming() {
        contextRunner
                .withPropertyValues("langchain4j.ollama.baseUrl=%s".formatted(getBaseUrl()))
                .run(context -> {
                    OllamaStreamingChatModel model = context.getBean(OllamaStreamingChatModel.class);
                    CompletableFuture<Response<AiMessage>> future = new CompletableFuture<>();
                    model.generate("What is the capital of Italy?", new StreamingResponseHandler<>() {
                        @Override
                        public void onNext(String token) {
                        }

                        @Override
                        public void onComplete(Response<AiMessage> response) {
                            future.complete(response);
                        }

                        @Override
                        public void onError(Throwable error) {
                        }
                    });

                    Response<AiMessage> response = future.get(30, SECONDS);
                    log.info("Response: " + response);
                    assertThat(response.content().text()).containsIgnoringCase("Rome");
                });
    }

    @Test
    void embedding() {
        contextRunner
                .withPropertyValues("langchain4j.ollama.baseUrl=%s".formatted(getBaseUrl()))
                .run(context -> {
                    OllamaEmbeddingModel model = context.getBean(OllamaEmbeddingModel.class);
                    Embedding embedding = model.embed("hi").content();
                    assertThat(embedding.dimension()).isEqualTo(3200);
                });
    }

}
