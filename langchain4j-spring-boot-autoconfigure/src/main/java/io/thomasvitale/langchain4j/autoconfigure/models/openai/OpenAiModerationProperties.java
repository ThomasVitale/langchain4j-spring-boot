package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.openai.OpenAiModerationOptions;

/**
 * Configuration properties for OpenAI moderation clients.
 */
@ConfigurationProperties(prefix = OpenAiModerationProperties.CONFIG_PREFIX)
public class OpenAiModerationProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.moderation";

    /**
     * Model options.
     */
    @NestedConfigurationProperty
    private OpenAiModerationOptions options = OpenAiModerationOptions.builder().build();

    public OpenAiModerationOptions getOptions() {
        return options;
    }

    public void setOptions(OpenAiModerationOptions options) {
        this.options = options;
    }

}
