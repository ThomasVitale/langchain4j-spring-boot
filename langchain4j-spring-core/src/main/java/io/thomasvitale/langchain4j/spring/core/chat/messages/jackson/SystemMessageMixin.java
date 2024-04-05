package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.langchain4j.data.message.SystemMessage;

/**
 * Mixin used to serialize / deserialize {@link SystemMessage}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class SystemMessageMixin {

    @JsonCreator
    SystemMessageMixin(@JsonProperty("text") String text) {}

}
