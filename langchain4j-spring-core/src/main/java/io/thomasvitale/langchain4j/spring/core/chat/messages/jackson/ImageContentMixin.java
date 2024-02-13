package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;

/**
 * Mixin used to serialize / deserialize {@link ImageContent}.
 *
 * @author Thomas Vitale
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageContentMixin {

    @JsonCreator
    ImageContentMixin(@JsonProperty("image") Image image,
            @JsonProperty("detailLevel") ImageContent.DetailLevel detailLevel) {
    }

}
