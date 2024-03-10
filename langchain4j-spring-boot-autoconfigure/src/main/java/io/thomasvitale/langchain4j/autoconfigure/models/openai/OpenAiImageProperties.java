package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.openai.OpenAiImageOptions;

/**
 * Configuration properties for OpenAI image clients.
 */
@ConfigurationProperties(prefix = OpenAiImageProperties.CONFIG_PREFIX)
public class OpenAiImageProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.image";

    /**
     * Model options.
     */
    @NestedConfigurationProperty
    private OpenAiImageOptions options = OpenAiImageOptions.builder().build();

    public OpenAiImageOptions getOptions() {
        return options;
    }

    public void setOptions(OpenAiImageOptions options) {
        this.options = options;
    }

}
