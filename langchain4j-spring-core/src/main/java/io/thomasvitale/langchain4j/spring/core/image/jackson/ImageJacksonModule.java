package io.thomasvitale.langchain4j.spring.core.image.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.data.image.Image;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the image functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new ImageJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @see LangChain4jJacksonModules
 */
public class ImageJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Image.class, ImageMixin.class);
        context.setMixInAnnotations(Image.Builder.class, ImageBuilderMixin.class);
    }

}
