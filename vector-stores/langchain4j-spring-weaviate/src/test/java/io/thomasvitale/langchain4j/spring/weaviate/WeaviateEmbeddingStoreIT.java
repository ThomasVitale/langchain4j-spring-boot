package io.thomasvitale.langchain4j.spring.weaviate;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.weaviate.WeaviateContainer;

import io.thomasvitale.langchain4j.spring.weaviate.client.WeaviateClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WeaviateEmbeddingStore}.
 * <p>
 * Adapted from WeaviateEmbeddingStoreIT in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
@Testcontainers
class WeaviateEmbeddingStoreIT {

    @Container
    static WeaviateContainer weaviate = new WeaviateContainer("semitechnologies/weaviate:1.23.10");

    private static WeaviateEmbeddingStore weaviateEmbeddingStore;

    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    @BeforeAll
    static void beforeAll() {
        WeaviateClientConfig clientConfig = WeaviateClientConfig.builder().host(weaviate.getHttpHostAddress()).build();
        weaviateEmbeddingStore = WeaviateEmbeddingStore.builder()
                .clientConfig(clientConfig)
                .objectClassName("LangChain4jTestObject")
                .build();
    }

    @Test
    void similaritySearchFromDocumentsWithoutContent() {
        var textSegment = TextSegment.from("hello");
        var embedding = embeddingModel.embed(textSegment.text()).content();

        var id = weaviateEmbeddingStore.add(embedding);
        assertThat(id).isNotBlank();

        var relevantTextSegments = weaviateEmbeddingStore.findRelevant(embedding, 10);
        assertThat(relevantTextSegments).hasSize(1);

        var textSegmentEmbeddingMatch = relevantTextSegments.get(0);
        assertThat(textSegmentEmbeddingMatch.score()).isCloseTo(1, Percentage.withPercentage(1));
        assertThat(textSegmentEmbeddingMatch.embeddingId()).isEqualTo(id);
        assertThat(textSegmentEmbeddingMatch.embedding()).isEqualTo(embedding);
        assertThat(textSegmentEmbeddingMatch.embedded()).isNull();
    }

    @Test
    void similaritySearchFromDocumentsWithContent() {
        var textSegment = TextSegment.from("hygge");
        var embedding = embeddingModel.embed(textSegment.text()).content();

        var id = weaviateEmbeddingStore.add(embedding, textSegment);
        assertThat(id).isNotBlank();

        var relevantTextSegments = weaviateEmbeddingStore.findRelevant(embedding, 10);
        assertThat(relevantTextSegments).hasSizeGreaterThanOrEqualTo(1);

        var textSegmentEmbeddingMatch = relevantTextSegments.get(0);
        assertThat(textSegmentEmbeddingMatch.score()).isCloseTo(1, Percentage.withPercentage(1));
        assertThat(textSegmentEmbeddingMatch.embeddingId()).isEqualTo(id);
        assertThat(textSegmentEmbeddingMatch.embedding()).isEqualTo(embedding);
        assertThat(textSegmentEmbeddingMatch.embedded()).isEqualTo(textSegment);
    }

}
