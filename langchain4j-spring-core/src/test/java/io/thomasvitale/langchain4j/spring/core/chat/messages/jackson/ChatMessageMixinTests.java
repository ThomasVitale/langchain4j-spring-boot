package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;

import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ChatMessageMixin}.
 */
class ChatMessageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeChatMessageForAiType() throws JsonProcessingException {
        var message = AiMessage.from("Simple answer");

        var json = objectMapper.writeValueAsString(message);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage.type()).isEqualTo(ChatMessageType.AI);
    }

    @Test
    void serializeAndDeserializeChatMessageForSystemType() throws JsonProcessingException {
        var message = SystemMessage.from("Simple instruction");

        var json = objectMapper.writeValueAsString(message);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage.type()).isEqualTo(ChatMessageType.SYSTEM);
    }

    @Test
    void serializeAndDeserializeChatMessageForToolExecutionResultType() throws JsonProcessingException {
        var message = ToolExecutionResultMessage.from("42", "database", "answer");

        var json = objectMapper.writeValueAsString(message);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage.type()).isEqualTo(ChatMessageType.TOOL_EXECUTION_RESULT);
    }

    @Test
    void serializeAndDeserializeChatMessageForUserType() throws JsonProcessingException {
        var message = UserMessage.from("Simple question");

        var json = objectMapper.writeValueAsString(message);

        var deserializedMessage = objectMapper.readValue(json, ChatMessage.class);
        assertThat(deserializedMessage.type()).isEqualTo(ChatMessageType.USER);
    }

}
