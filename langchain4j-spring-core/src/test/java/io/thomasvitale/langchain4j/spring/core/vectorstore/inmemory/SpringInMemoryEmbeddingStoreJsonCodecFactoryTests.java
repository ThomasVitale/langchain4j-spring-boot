package io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory;

import io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory.SpringInMemoryEmbeddingStoreJsonCodecFactory;
import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory.jackson.JacksonInMemoryEmbeddingStoreJsonCodec;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SpringInMemoryEmbeddingStoreJsonCodecFactory}.
 *
 * @author Thomas Vitale
 */
class SpringInMemoryEmbeddingStoreJsonCodecFactoryTests {

    @Test
    void factoryProducesJacksonImplementation() {
        var jsonCodecFactory = new SpringInMemoryEmbeddingStoreJsonCodecFactory();
        var jsonCodec = jsonCodecFactory.create();
        assertThat(jsonCodec).isInstanceOf(JacksonInMemoryEmbeddingStoreJsonCodec.class);
    }

}
