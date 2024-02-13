package io.thomasvitale.langchain4j.spring.core.document.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.data.document.Metadata;

import dev.langchain4j.data.segment.TextSegment;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the document functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new DocumentJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @author Thomas Vitale
 * @see LangChain4jJacksonModules
 */
public class DocumentJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Metadata.class, MetadataMixin.class);
        context.setMixInAnnotations(TextSegment.class, TextSegmentMixin.class);
    }

}
