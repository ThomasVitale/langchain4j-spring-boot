package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.UserMessage;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;

/**
 * Mixin used to serialize / deserialize {@link UserMessage}.
 *
 * @author Thomas Vitale
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = UserMessageMixin.UserMessageDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class UserMessageMixin {

    static class UserMessageDeserializer extends JsonDeserializer<UserMessage> {

        private static final TypeReference<List<Content>> CONTENT_LIST = new TypeReference<>() {
        };

        @Override
        public UserMessage deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            JsonNode node = objectMapper.readTree(jsonParser);

            String name = null;
            if (node.has("name")) {
                name = node.get("name").asText();
            }

            String text = null;
            if (node.has("text")) {
                text = node.get("text").asText();
            }

            List<Content> contents = null;
            if (node.has("contents")) {
                contents = objectMapper.readValue(node.get("contents").traverse(objectMapper), CONTENT_LIST);
            }

            if (text != null) {
                return buildUserMessageWithText(name, text);
            }
            else if (contents != null) {
                return buildUserMessageWithContents(name, contents);
            }
            else {
                throw new JsonDeserializationException(
                        "No 'text' or 'contents' fields found in %s".formatted(UserMessage.class.getName()));
            }
        }

        private UserMessage buildUserMessageWithText(String name, String text) {
            return name == null ? new UserMessage(text) : new UserMessage(name, text);
        }

        private UserMessage buildUserMessageWithContents(String name, List<Content> contents) {
            return name == null ? new UserMessage(contents) : new UserMessage(name, contents);
        }

    }

}
