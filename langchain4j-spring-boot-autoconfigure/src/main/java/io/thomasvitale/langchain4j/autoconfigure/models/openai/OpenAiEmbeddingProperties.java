package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.openai.OpenAiEmbeddingOptions;

/**
 * Configuration properties for OpenAI embedding models.
 */
@ConfigurationProperties(prefix = OpenAiEmbeddingProperties.CONFIG_PREFIX)
public class OpenAiEmbeddingProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.embedding";

    /**
     * Model options.
     */
    @NestedConfigurationProperty
    private OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder().build();

    public OpenAiEmbeddingOptions getOptions() {
        return options;
    }

    public void setOptions(OpenAiEmbeddingOptions options) {
        this.options = options;
    }

}
