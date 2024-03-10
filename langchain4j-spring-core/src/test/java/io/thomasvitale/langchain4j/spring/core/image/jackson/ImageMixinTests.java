package io.thomasvitale.langchain4j.spring.core.image.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.image.Image;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ImageMixin}.
 */
class ImageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeImageWithUrl() throws JsonProcessingException, JSONException {
        var image = Image.builder().url("http://example.net").revisedPrompt("something funny").build();

        var json = objectMapper.writeValueAsString(image);

        JSONAssert.assertEquals("""
                    {
                        "url": "http://example.net",
                        "revisedPrompt": "something funny"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedImage = objectMapper.readValue(json, Image.class);
        assertThat(deserializedImage).isEqualTo(image);
    }

    @Test
    void serializeAndDeserializeImageWithBase64AndMimeType() throws JsonProcessingException, JSONException {
        var image = Image.builder()
            .base64Data(Base64.getEncoder().encodeToString("image".getBytes()))
            .mimeType("img/png")
            .build();

        var json = objectMapper.writeValueAsString(image);

        JSONAssert.assertEquals("""
                    {
                        "base64Data": "aW1hZ2U=",
                        "mimeType": "img/png"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedImage = objectMapper.readValue(json, Image.class);
        assertThat(deserializedImage).isEqualTo(image);
    }

}
