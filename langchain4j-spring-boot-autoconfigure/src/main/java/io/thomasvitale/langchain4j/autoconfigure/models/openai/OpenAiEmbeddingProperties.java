package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for OpenAI embedding clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OpenAiEmbeddingProperties.CONFIG_PREFIX)
public class OpenAiEmbeddingProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.embedding";

    /**
     * Name of the model to use.
     */
    private String model = "text-embedding-ada-002";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
