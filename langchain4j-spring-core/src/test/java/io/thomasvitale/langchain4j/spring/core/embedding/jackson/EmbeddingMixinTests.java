package io.thomasvitale.langchain4j.spring.core.embedding.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.embedding.Embedding;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EmbeddingMixin}.
 */
class EmbeddingMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeEmbedding() throws JsonProcessingException, JSONException {
        var embedding = new Embedding(new float[] { 1.0f, 2.0f, 3.0f });

        var json = objectMapper.writeValueAsString(embedding);

        JSONAssert.assertEquals("""
                    {
                        "vector": [1.0, 2.0, 3.0]
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedEmbedding = objectMapper.readValue(json, Embedding.class);
        assertThat(deserializedEmbedding).isEqualTo(embedding);
    }

}
