package io.thomasvitale.langchain4j.spring.ollama;

import java.net.URI;
import java.util.List;

import dev.langchain4j.data.segment.TextSegment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaEmbeddingModel}.
 */
@Testcontainers
class OllamaEmbeddingModelIT {

    private static final String MODEL_NAME = "orca-mini";

    @Container
    static OllamaContainer ollama = new OllamaContainer(DockerImageName
            .parse("ghcr.io/thomasvitale/ollama-%s".formatted(MODEL_NAME))
            .asCompatibleSubstituteFor("ollama/ollama"));

    private static OllamaClient ollamaClient;

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434));
    }

    @BeforeAll
    static void beforeAll() {
        ollamaClient = new OllamaClient(OllamaClientConfig.builder().baseUrl(URI.create(getBaseUrl())).build(),
                RestClient.builder());
    }

    @Test
    void generateSingleEmbedding() {
        var ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().build())
            .build();

        var response = ollamaEmbeddingModel.embed("Welcome to the jungle");

        assertThat(response.content().vector()).isNotEmpty();
        assertThat(response.finishReason()).isNull();
        assertThat(response.tokenUsage()).isNull();
    }

    @Test
    void generateEmbeddings() {
        var ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().build())
            .build();

        var textSegment1 = TextSegment.from("Welcome to the jungle");
        var textSegment2 = TextSegment.from("Welcome to Jumanji");

        var response = ollamaEmbeddingModel.embedAll(List.of(textSegment1, textSegment2));

        assertThat(response.content().size()).isEqualTo(2);
        assertThat(response.finishReason()).isNull();
        assertThat(response.tokenUsage()).isNull();
    }

}
