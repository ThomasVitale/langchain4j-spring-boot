package io.thomasvitale.langchain4j.spring.core.json;

import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.json.jackson.JacksonJsonCodec;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SpringJsonCodecFactory}.
 */
class SpringJsonCodecFactoryTests {

    @Test
    void factoryProducesJacksonImplementation() {
        var jsonCodecFactory = new SpringJsonCodecFactory();
        var jsonCodec = jsonCodecFactory.create();
        assertThat(jsonCodec).isInstanceOf(JacksonJsonCodec.class);
    }

}
