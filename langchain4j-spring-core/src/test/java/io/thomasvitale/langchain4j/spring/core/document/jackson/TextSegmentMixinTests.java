package io.thomasvitale.langchain4j.spring.core.document.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Mixin used to serialize / deserialize {@link TextSegmentMixin}.
 *
 * @author Thomas Vitale
 */
class TextSegmentMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeTextSegment() throws JsonProcessingException, JSONException {
        var textSegment = TextSegment.from("document", Metadata.from("key", "value"));

        var json = objectMapper.writeValueAsString(textSegment);

        JSONAssert.assertEquals("""
                    {
                        "text": "document",
                        "metadata" : {
                            "metadata": {
                                "key": "value"
                            }
                        }
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedTextSegment = objectMapper.readValue(json, TextSegment.class);
        assertThat(deserializedTextSegment).isEqualTo(textSegment);
    }

}
