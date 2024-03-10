package io.thomasvitale.langchain4j.spring.core.tool.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.agent.tool.ToolExecutionRequest;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the tool functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new ToolJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @see LangChain4jJacksonModules
 */
public class ToolJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(ToolExecutionRequest.class, ToolExecutionRequestMixin.class);
        context.setMixInAnnotations(ToolExecutionRequest.Builder.class, ToolExecutionRequestBuilderMixin.class);
    }

}
