package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link TextContentMixin}.
 */
class TextContentMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeTextContent() throws JsonProcessingException, JSONException {
        var content = TextContent.from("Simple answer");

        var json = objectMapper.writeValueAsString(content);

        JSONAssert.assertEquals("""
                    {
                        "text": "Simple answer",
                        "type": "TEXT"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedContent = objectMapper.readValue(json, Content.class);
        assertThat(deserializedContent).isEqualTo(content);
    }

}
