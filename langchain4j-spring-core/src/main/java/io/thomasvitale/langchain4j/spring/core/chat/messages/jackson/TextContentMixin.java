package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.langchain4j.data.message.TextContent;

/**
 * Mixin used to serialize / deserialize {@link TextContent}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class TextContentMixin {

    @JsonCreator
    TextContentMixin(@JsonProperty("text") String text) {}

}
