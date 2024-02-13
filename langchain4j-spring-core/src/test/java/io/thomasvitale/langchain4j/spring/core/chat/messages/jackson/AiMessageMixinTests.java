package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AiMessageMixin}.
 *
 * @author Thomas Vitale
 */
class AiMessageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeAiMessageWithText() throws JsonProcessingException, JSONException {
        var message = AiMessage.from("Simple answer");

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "text": "Simple answer",
                        "type": "AI"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

    @Test
    void serializeAndDeserializeAiMessageWithToolExecutionRequest() throws JsonProcessingException, JSONException {
        var message = AiMessage.from(ToolExecutionRequest.builder().name("queryDatabase").arguments("{}").build());

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "toolExecutionRequests": [{
                            "name": "queryDatabase",
                            "arguments": "{}"
                        }],
                        "type": "AI"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

}
