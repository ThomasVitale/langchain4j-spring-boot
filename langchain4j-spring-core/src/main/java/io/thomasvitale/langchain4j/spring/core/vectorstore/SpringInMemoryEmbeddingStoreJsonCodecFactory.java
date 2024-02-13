package io.thomasvitale.langchain4j.spring.core.vectorstore;

import dev.langchain4j.spi.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodecFactory;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodec;

import io.thomasvitale.langchain4j.spring.core.vectorstore.jackson.JacksonInMemoryEmbeddingStoreJsonCodec;

/**
 * A factory for creating a {@link InMemoryEmbeddingStoreJsonCodec} instance.
 *
 * @author Thomas Vitale
 */
public class SpringInMemoryEmbeddingStoreJsonCodecFactory implements InMemoryEmbeddingStoreJsonCodecFactory {

    @Override
    public InMemoryEmbeddingStoreJsonCodec create() {
        return new JacksonInMemoryEmbeddingStoreJsonCodec();
    }

}
