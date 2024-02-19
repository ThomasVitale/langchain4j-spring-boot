package io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory.jackson;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link InMemoryEmbeddingStoreMixin}.
 *
 * @author Thomas Vitale
 */
class InMemoryEmbeddingStoreMixinTests {

    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeInMemoryEmbeddingStore() throws JsonProcessingException {
        var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();

        TextSegment textSegment = TextSegment.from("first");
        Embedding embedding = embeddingModel.embed(textSegment).content();
        embeddingStore.add(embedding, textSegment);

        var json = objectMapper.writeValueAsString(embeddingStore);

        var deserializedEmbeddingStore = objectMapper.readValue(json, InMemoryEmbeddingStore.class);

        var expectedEntries = ReflectionTestUtils.getField(embeddingStore, "entries");
        var actualEntries = ReflectionTestUtils.getField(deserializedEmbeddingStore, "entries");
        assertThat(expectedEntries).isEqualTo(actualEntries);
    }

    @Test
    void serializeAndDeserializeInMemoryEmbeddingStoreWithMetadata() throws JsonProcessingException {
        var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();

        TextSegment textSegment = TextSegment.from("second", Metadata.from(Map.of("key1", "value1", "key2", "value2")));
        Embedding embedding = embeddingModel.embed(textSegment).content();
        embeddingStore.add(embedding, textSegment);

        var json = objectMapper.writeValueAsString(embeddingStore);

        var deserializedEmbeddingStore = objectMapper.readValue(json, InMemoryEmbeddingStore.class);

        var expectedEntries = ReflectionTestUtils.getField(embeddingStore, "entries");
        var actualEntries = ReflectionTestUtils.getField(deserializedEmbeddingStore, "entries");
        assertThat(expectedEntries).isEqualTo(actualEntries);
    }

}
