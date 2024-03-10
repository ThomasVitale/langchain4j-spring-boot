package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;

import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ContentMixin}.
 */
class ContentMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeContentForTextType() throws JsonProcessingException {
        var content = TextContent.from("Simple answer");

        var json = objectMapper.writeValueAsString(content);

        var deserializedContent = objectMapper.readValue(json, Content.class);
        assertThat(deserializedContent.type()).isEqualTo(ContentType.TEXT);
    }

    @Test
    void serializeAndDeserializeContentForImageType() throws JsonProcessingException {
        var content = ImageContent.from("example.net");

        var json = objectMapper.writeValueAsString(content);

        var deserializedContent = objectMapper.readValue(json, Content.class);
        assertThat(deserializedContent.type()).isEqualTo(ContentType.IMAGE);
    }

}
