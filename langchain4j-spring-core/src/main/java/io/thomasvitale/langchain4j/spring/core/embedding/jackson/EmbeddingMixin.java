package io.thomasvitale.langchain4j.spring.core.embedding.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.langchain4j.data.embedding.Embedding;

/**
 * Mixin used to serialize / deserialize {@link Embedding}.
 *
 * @author Thomas Vitale
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class EmbeddingMixin {

    @JsonCreator
    EmbeddingMixin(@JsonProperty("vector") float[] vector) {
    }

}
