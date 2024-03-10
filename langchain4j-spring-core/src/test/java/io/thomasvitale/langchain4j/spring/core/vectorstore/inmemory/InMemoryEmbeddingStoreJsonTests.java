package io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory;

import java.util.Map;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link InMemoryEmbeddingStore#serializeToJson()} and
 * {@link InMemoryEmbeddingStore#fromJson(String)}}.
 * <p>
 * Adapted from InMemoryEmbeddingStoreTest in the LangChain4j project.
 */
public class InMemoryEmbeddingStoreJsonTests {

    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    @Test
    void serializeAndDeserializeInMemoryEmbeddingStore() {
        var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();

        TextSegment textSegment = TextSegment.from("document",
                Metadata.from(Map.of("key1", "value1", "key2", "value2")));
        Embedding embedding = embeddingModel.embed(textSegment).content();
        embeddingStore.add(embedding, textSegment);

        var json = embeddingStore.serializeToJson();

        var deserializedEmbeddingStore = InMemoryEmbeddingStore.fromJson(json);

        var expectedEntries = ReflectionTestUtils.getField(embeddingStore, "entries");
        var actualEntries = ReflectionTestUtils.getField(deserializedEmbeddingStore, "entries");
        assertThat(expectedEntries).isEqualTo(actualEntries);
    }

}
