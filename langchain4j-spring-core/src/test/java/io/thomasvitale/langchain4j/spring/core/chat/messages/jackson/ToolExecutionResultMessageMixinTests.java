package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.ToolExecutionResultMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ToolExecutionResultMessageMixin}.
 */
class ToolExecutionResultMessageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeToolExecutionResultMessage() throws JsonProcessingException, JSONException {
        var message = ToolExecutionResultMessage.from("42", "queryDatabase", "enrolled");

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "id": "42",
                        "toolName": "queryDatabase",
                        "text": "enrolled",
                        "type": "TOOL_EXECUTION_RESULT"
                      }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, ToolExecutionResultMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

}
