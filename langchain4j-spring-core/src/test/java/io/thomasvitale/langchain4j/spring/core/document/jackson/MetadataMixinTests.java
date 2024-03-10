package io.thomasvitale.langchain4j.spring.core.document.jackson;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.document.Metadata;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link MetadataMixin}.
 */
class MetadataMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeMetadata() throws JsonProcessingException, JSONException {
        var metadata = new Metadata(Map.of("key1", "value1", "key2", "value2"));

        var json = objectMapper.writeValueAsString(metadata);

        JSONAssert.assertEquals("""
                    {
                        "metadata": {
                            "key1": "value1",
                            "key2": "value2"
                        }
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedMetadata = objectMapper.readValue(json, Metadata.class);
        assertThat(deserializedMetadata).isEqualTo(metadata);
    }

}
