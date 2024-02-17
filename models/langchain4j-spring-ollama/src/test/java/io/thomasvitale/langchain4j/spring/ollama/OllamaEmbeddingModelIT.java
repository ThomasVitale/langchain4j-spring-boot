package io.thomasvitale.langchain4j.spring.ollama;

import java.util.List;

import dev.langchain4j.data.segment.TextSegment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.spring.core.model.client.ClientConfig;
import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaEmbeddingModel}.
 *
 * @author Thomas Vitale
 */
@Testcontainers
class OllamaEmbeddingModelIT {

    private static final Logger logger = LoggerFactory.getLogger(OllamaEmbeddingModelIT.class);

    private static final String MODEL_NAME = "orca-mini";

    @Container
    static GenericContainer<?> ollama = new GenericContainer<>("ghcr.io/thomasvitale/ollama-%s".formatted(MODEL_NAME))
        .withExposedPorts(11434);

    private static OllamaClient ollamaClient;

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434));
    }

    @BeforeAll
    static void beforeAll() {
        ollamaClient = new OllamaClient(getBaseUrl(), RestClient.builder(), ClientConfig.create());
    }

    @Test
    void generateSingleEmbedding() {
        var ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
            .withClient(ollamaClient)
            .withModel(MODEL_NAME)
            .withOptions(Options.builder().build())
            .build();

        var response = ollamaEmbeddingModel.embed("Welcome to the jungle");

        assertThat(response.content().vector()).isNotEmpty();
        assertThat(response.finishReason()).isNull();
        assertThat(response.tokenUsage()).isNull();
    }

    @Test
    void generateEmbeddings() {
        var ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
            .withClient(ollamaClient)
            .withModel(MODEL_NAME)
            .withOptions(Options.builder().build())
            .build();

        var textSegment1 = TextSegment.from("Welcome to the jungle");
        var textSegment2 = TextSegment.from("Welcome to Jumanji");

        var response = ollamaEmbeddingModel.embedAll(List.of(textSegment1, textSegment2));

        assertThat(response.content().size()).isEqualTo(2);
        assertThat(response.finishReason()).isNull();
        assertThat(response.tokenUsage()).isNull();
    }

}