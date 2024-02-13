package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageJsonCodec;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;
import io.thomasvitale.langchain4j.spring.core.json.JsonSerializationException;
import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import org.springframework.util.StringUtils;

/**
 * A codec for serializing/deserializing {@link ChatMessage} objects using Jackson.
 * <p>
 * Adapted from GsonChatMessageJsonCodec in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
public class JacksonChatMessageJsonCodec implements ChatMessageJsonCodec {

    private static final TypeReference<List<ChatMessage>> MESSAGE_LIST_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public JacksonChatMessageJsonCodec() {
        this.objectMapper = LangChain4jJacksonProvider.getObjectMapper();
    }

    @Override
    public ChatMessage messageFromJson(String json) {
        try {
            return objectMapper.readValue(json, ChatMessage.class);
        }
        catch (JsonProcessingException ex) {
            throw new JsonDeserializationException(ex);
        }
    }

    @Override
    public List<ChatMessage> messagesFromJson(String json) {
        if (!StringUtils.hasText(json)) {
            return List.of();
        }

        try {
            return objectMapper.readValue(json, MESSAGE_LIST_TYPE);
        }
        catch (JsonProcessingException ex) {
            throw new JsonDeserializationException(ex);
        }
    }

    @Override
    public String messageToJson(ChatMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        }
        catch (JsonProcessingException ex) {
            throw new JsonSerializationException(ex);
        }
    }

    @Override
    public String messagesToJson(List<ChatMessage> messages) {
        try {
            return objectMapper.writeValueAsString(messages);
        }
        catch (JsonProcessingException ex) {
            throw new JsonSerializationException(ex);
        }
    }

}
