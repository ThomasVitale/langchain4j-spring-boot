package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.SystemMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SystemMessageMixin}.
 */
class SystemMessageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeSystemMessage() throws JsonProcessingException, JSONException {
        var message = SystemMessage.from("You're a nice assistant");

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "text": "You're a nice assistant",
                        "type": "SYSTEM"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, SystemMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

}
