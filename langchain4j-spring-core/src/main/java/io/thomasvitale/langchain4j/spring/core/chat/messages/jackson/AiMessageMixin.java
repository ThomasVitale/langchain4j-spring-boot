package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;

/**
 * Mixin used to serialize / deserialize {@link AiMessage}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = AiMessageMixin.AiMessageDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AiMessageMixin {

    @JsonCreator
    AiMessageMixin(@JsonProperty("toolExecutionRequests") List<ToolExecutionRequest> toolExecutionRequests) {
    }

    static class AiMessageDeserializer extends JsonDeserializer<AiMessage> {

        private static final TypeReference<List<ToolExecutionRequest>> TOOL_EXECUTION_REQUEST_LIST = new TypeReference<>() {
        };

        @Override
        public AiMessage deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            JsonNode node = objectMapper.readTree(jsonParser);
            if (node.has("text")) {
                return new AiMessage(node.get("text").asText());
            }
            else if (node.has("toolExecutionRequests")) {
                List<ToolExecutionRequest> toolExecutionRequests = objectMapper
                    .readValue(node.get("toolExecutionRequests").traverse(objectMapper), TOOL_EXECUTION_REQUEST_LIST);
                return new AiMessage(toolExecutionRequests);
            }
            else {
                throw new JsonDeserializationException(
                        "No 'text' or 'toolExecutionRequests' fields found in %s".formatted(AiMessage.class.getName()));
            }
        }

    }

}
