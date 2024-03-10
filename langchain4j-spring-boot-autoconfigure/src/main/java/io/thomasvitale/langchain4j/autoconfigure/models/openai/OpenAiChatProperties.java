package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.openai.OpenAiChatOptions;

/**
 * Configuration properties for OpenAI chat models.
 */
@ConfigurationProperties(prefix = OpenAiChatProperties.CONFIG_PREFIX)
public class OpenAiChatProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.chat";

    /**
     * Model options.
     */
    @NestedConfigurationProperty
    private OpenAiChatOptions options = OpenAiChatOptions.builder().build();

    public OpenAiChatOptions getOptions() {
        return options;
    }

    public void setOptions(OpenAiChatOptions options) {
        this.options = options;
    }

}
