package io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodec;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;
import io.thomasvitale.langchain4j.spring.core.json.JsonSerializationException;
import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

/**
 * A codec for serializing/deserializing {@link InMemoryEmbeddingStore} objects using
 * Jackson.
 * <p>
 * Adapted from GsonInMemoryEmbeddingStoreJsonCodec in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
public class JacksonInMemoryEmbeddingStoreJsonCodec implements InMemoryEmbeddingStoreJsonCodec {

    private static final TypeReference<InMemoryEmbeddingStore<TextSegment>> TEXT_SEGMENT_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public JacksonInMemoryEmbeddingStoreJsonCodec() {
        this.objectMapper = LangChain4jJacksonProvider.getObjectMapper();
    }

    @Override
    public InMemoryEmbeddingStore<TextSegment> fromJson(String json) {
        try {
            return objectMapper.readValue(json, TEXT_SEGMENT_TYPE);
        }
        catch (JsonProcessingException ex) {
            throw new JsonDeserializationException(ex);
        }
    }

    @Override
    public String toJson(InMemoryEmbeddingStore<?> embeddingStore) {
        try {
            return objectMapper.writeValueAsString(embeddingStore);
        }
        catch (JsonProcessingException ex) {
            throw new JsonSerializationException(ex);
        }
    }

}
