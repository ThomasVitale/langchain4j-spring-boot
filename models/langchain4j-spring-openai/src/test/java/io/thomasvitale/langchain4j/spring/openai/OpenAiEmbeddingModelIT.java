package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingModels;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAiEmbeddingModel}.
 * <p>
 * Adapted from OpenAiEmbeddingModelIT in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
class OpenAiEmbeddingModelIT {

    private static OpenAiClient openAiClient;

    @BeforeAll
    static void beforeAll() {
        String apiKey = System.getenv("LANGCHAIN4J_OPENAI_CLIENT_API_KEY");
        openAiClient = new OpenAiClient(
                OpenAiClientConfig.builder().apiKey(apiKey).build(),
                RestClient.builder());
    }

    @Test
    void generateSingleEmbedding() {
        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .client(openAiClient)
                .options(OpenAiEmbeddingOptions.builder()
                        .model(EmbeddingModels.TEXT_EMBEDDING_3_SMALL.toString()).build())
                .build();

        String text = "Welcome to the jungle";

        Response<Embedding> response = embeddingModel.embed(text);

        assertThat(response.content().vector()).hasSize(1536);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isEqualTo(4);
        assertThat(tokenUsage.outputTokenCount()).isNull();
        assertThat(tokenUsage.totalTokenCount()).isEqualTo(4);

        assertThat(response.finishReason()).isNull();
    }

    @Test
    void generateMultipleEmbeddings() {
        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .client(openAiClient)
                .options(OpenAiEmbeddingOptions.builder().build())
                .build();

        TextSegment textSegment1 = TextSegment.from("Welcome to the jungle");
        TextSegment textSegment2 = TextSegment.from("Welcome to Jumanji");

        Response<List<Embedding>> response = embeddingModel.embedAll(List.of(textSegment1, textSegment2));

        assertThat(response.content()).hasSize(2);
        assertThat(response.content().get(0).dimension()).isEqualTo(1536);
        assertThat(response.content().get(1).dimension()).isEqualTo(1536);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isEqualTo(9);
        assertThat(tokenUsage.outputTokenCount()).isNull();
        assertThat(tokenUsage.totalTokenCount()).isEqualTo(9);

        assertThat(response.finishReason()).isNull();
    }

    @Test
    void generateEmbeddingWithDimension() {
        int dimensions = 42;

        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .client(openAiClient)
                .options(OpenAiEmbeddingOptions.builder()
                        .model(EmbeddingModels.TEXT_EMBEDDING_3_SMALL.toString())
                        .dimensions(dimensions).build())
                .build();

        String text = "Welcome to the jungle";

        Response<Embedding> response = embeddingModel.embed(text);

        assertThat(response.content().dimension()).isEqualTo(dimensions);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isEqualTo(4);
        assertThat(tokenUsage.outputTokenCount()).isNull();
        assertThat(tokenUsage.totalTokenCount()).isEqualTo(4);

        assertThat(response.finishReason()).isNull();
    }

}
