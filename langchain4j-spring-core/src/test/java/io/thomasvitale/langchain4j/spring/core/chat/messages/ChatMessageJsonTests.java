package io.thomasvitale.langchain4j.spring.core.chat.messages;

import java.util.List;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ChatMessageSerializer} and {@link ChatMessageDeserializer}.
 * <p>
 * Adapted from ChatMessageSerializerTest in the LangChain4j project.
 */
class ChatMessageJsonTests {

    @Test
    void serializeAndDeserializeSingleMessage() throws JSONException {
        var message = SystemMessage.systemMessage("You are a funny assistant");

        var json = ChatMessageSerializer.messageToJson(message);

        JSONAssert.assertEquals("""
                    {
                        "text": "You are a funny assistant",
                        "type": "SYSTEM"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = ChatMessageDeserializer.messageFromJson(json);
        assertThat(deserializedMessage).isEqualTo(message);
    }

    @Test
    void serializeAndDeserializeMultipleMessages() throws JSONException {
        var messages = List.of(UserMessage.userMessage("simple question"), AiMessage.aiMessage("simple answer"));

        var json = ChatMessageSerializer.messagesToJson(messages);

        JSONAssert.assertEquals("""
                    [
                        {
                            "contents": [
                                {
                                    "text": "simple question",
                                    "type": "TEXT"
                                }
                            ],
                            "type": "USER"
                        },
                        {
                            "text": "simple answer",
                            "type": "AI"
                        }
                    ]
                """, json, JSONCompareMode.STRICT);

        var deserializedMessages = ChatMessageDeserializer.messagesFromJson(json);
        assertThat(deserializedMessages).isEqualTo(messages);
    }

    @Test
    void serializeAndDeserializeEmptyMessageList() {
        List<ChatMessage> messages = List.of();

        var json = ChatMessageSerializer.messagesToJson(messages);

        var messagesFromJson = ChatMessageDeserializer.messagesFromJson(json);
        assertThat(messagesFromJson).isEmpty();
    }

    @Test
    void deserializeNullAsEmptyList() {
        var messagesFromJson = ChatMessageDeserializer.messagesFromJson(null);
        assertThat(messagesFromJson).isEmpty();
    }

}
