package io.thomasvitale.langchain4j.spring.core.vectorstore.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the vector store functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new VectorStoreJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @author Thomas Vitale
 * @see LangChain4jJacksonModules
 */
public class VectorStoreJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(InMemoryEmbeddingStore.class, InMemoryEmbeddingStoreMixin.class);
    }

}
