package io.thomasvitale.langchain4j.spring.core.json;

import dev.langchain4j.internal.Json;
import dev.langchain4j.spi.json.JsonCodecFactory;

import io.thomasvitale.langchain4j.spring.core.json.jackson.JacksonJsonCodec;

/**
 * A factory for creating a {@link Json.JsonCodec} instance.
 */
public class SpringJsonCodecFactory implements JsonCodecFactory {

    @Override
    public Json.JsonCodec create() {
        return new JacksonJsonCodec();
    }

}
