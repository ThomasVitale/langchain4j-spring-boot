package io.thomasvitale.langchain4j.spring.core.embedding.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.data.embedding.Embedding;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the embedding functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new EmbeddingJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @see LangChain4jJacksonModules
 */
public class EmbeddingJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Embedding.class, EmbeddingMixin.class);
    }

}
