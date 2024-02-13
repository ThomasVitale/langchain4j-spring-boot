package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;

/**
 * Mixin used to serialize / deserialize {@link ChatMessage}.
 *
 * @author Thomas Vitale
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(value = { @JsonSubTypes.Type(value = AiMessage.class, name = "AI"),
        @JsonSubTypes.Type(value = SystemMessage.class, name = "SYSTEM"),
        @JsonSubTypes.Type(value = ToolExecutionResultMessage.class, name = "TOOL_EXECUTION_RESULT"),
        @JsonSubTypes.Type(value = UserMessage.class, name = "USER"), })
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ChatMessageMixin {

    @JsonProperty
    abstract ChatMessageType type();

}
