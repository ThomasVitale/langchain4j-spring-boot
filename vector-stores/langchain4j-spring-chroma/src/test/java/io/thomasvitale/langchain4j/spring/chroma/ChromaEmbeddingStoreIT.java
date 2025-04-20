package io.thomasvitale.langchain4j.spring.chroma;

import java.net.URI;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClient;
import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClientConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * Integration tests for {@link ChromaEmbeddingStore}.
 * <p>
 * Adapted from ChromaEmbeddingStoreIT in the LangChain4j project.
 */
@Testcontainers
class ChromaEmbeddingStoreIT {

    @Container
    static ChromaDBContainer chroma = new ChromaDBContainer("ghcr.io/chroma-core/chroma:0.5.3");

    private static ChromaClient chromaClient;

    private static ChromaEmbeddingStore chromaEmbeddingStore;

    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(chroma.getHost(), chroma.getMappedPort(8000));
    }

    @BeforeAll
    static void beforeAll() {
        chromaClient = new ChromaClient(ChromaClientConfig.builder().url(URI.create(getBaseUrl())).build(),
                RestClient.builder());
        chromaEmbeddingStore = ChromaEmbeddingStore.builder()
            .client(chromaClient)
            .collectionName("LangChain4jCollection")
            .build();
        chromaEmbeddingStore.afterPropertiesSet();
    }

    @Test
    void similaritySearchFromDocumentsWithMetadata() {
        var textSegment = TextSegment.from("hello", Metadata.from("test-key", "test-value"));
        var embedding = embeddingModel.embed(textSegment.text()).content();

        var id = chromaEmbeddingStore.add(embedding, textSegment);
        assertThat(id).isNotBlank();

        var textSegment2 = TextSegment.from("hello?");
        var embedding2 = embeddingModel.embed(textSegment.text()).content();
        chromaEmbeddingStore.add(embedding2, textSegment2);

        var relevantTextSegments = chromaEmbeddingStore.findRelevant(embedding, 1);
        assertThat(relevantTextSegments).hasSize(1);

        var textSegmentEmbeddingMatch = relevantTextSegments.get(0);
        assertThat(textSegmentEmbeddingMatch.score()).isCloseTo(1, withPercentage(1));
        assertThat(textSegmentEmbeddingMatch.embeddingId()).isEqualTo(id);
        assertThat(textSegmentEmbeddingMatch.embedding()).isEqualTo(embedding);
        assertThat(textSegmentEmbeddingMatch.embedded()).isEqualTo(textSegment);
    }

}
