package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ImageContentMixin}.
 *
 * @author Thomas Vitale
 */
class ImageContentMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeImageContentFromUri() throws JsonProcessingException, JSONException {
        var content = ImageContent.from("http://example.net");

        var json = objectMapper.writeValueAsString(content);

        JSONAssert.assertEquals("""
                    {
                        "detailLevel": "LOW",
                        "image": {
                            "url": "http://example.net"
                        },

                        "type": "IMAGE"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedContent = objectMapper.readValue(json, Content.class);
        assertThat(deserializedContent).isEqualTo(content);
    }

    @Test
    void serializeAndDeserializeImageContentFromUriAndDetailLevel() throws JsonProcessingException, JSONException {
        var content = ImageContent.from("http://example.net", ImageContent.DetailLevel.HIGH);

        var json = objectMapper.writeValueAsString(content);

        JSONAssert.assertEquals("""
                    {
                        "detailLevel": "HIGH",
                        "image": {
                            "url": "http://example.net"
                        },
                        "type": "IMAGE"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedContent = objectMapper.readValue(json, Content.class);
        assertThat(deserializedContent).isEqualTo(content);
    }

}
