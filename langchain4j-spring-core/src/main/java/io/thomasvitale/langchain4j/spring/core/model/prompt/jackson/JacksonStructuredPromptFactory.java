package io.thomasvitale.langchain4j.spring.core.model.prompt.jackson;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.spi.prompt.structured.StructuredPromptFactory;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

/**
 * A factory for creating a {@link Prompt} instance from a structured prompt using
 * Jackson.
 * <p>
 * Adapted from DefaultStructuredPromptFactory in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
public class JacksonStructuredPromptFactory implements StructuredPromptFactory {

    public static TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public JacksonStructuredPromptFactory() {
        this.objectMapper = LangChain4jJacksonProvider.getObjectMapper();
    }

    @Override
    public Prompt toPrompt(Object structuredPrompt) {
        StructuredPrompt annotation = StructuredPrompt.Util.validateStructuredPrompt(structuredPrompt);
        String promptTemplateString = StructuredPrompt.Util.join(annotation);

        Map<String, Object> variables = extractVariables(structuredPrompt);

        PromptTemplate promptTemplate = PromptTemplate.from(promptTemplateString);
        return promptTemplate.apply(variables);
    }

    private Map<String, Object> extractVariables(Object structuredPrompt) {
        return objectMapper.convertValue(structuredPrompt, MAP_TYPE);
    }

}
