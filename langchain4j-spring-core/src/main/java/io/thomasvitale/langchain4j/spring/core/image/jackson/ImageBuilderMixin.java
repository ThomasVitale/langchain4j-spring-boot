package io.thomasvitale.langchain4j.spring.core.image.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import dev.langchain4j.data.image.Image;

/**
 * Mixin used to configure {@link Image.Builder} for serialization/deserialization.
 *
 * @author Thomas Vitale
 */
@JsonPOJOBuilder(withPrefix = "")
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageBuilderMixin {

}
