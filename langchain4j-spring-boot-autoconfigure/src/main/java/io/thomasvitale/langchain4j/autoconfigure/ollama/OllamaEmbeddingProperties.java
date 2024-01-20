package io.thomasvitale.langchain4j.autoconfigure.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Ollama embedding.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OllamaEmbeddingProperties.CONFIG_PREFIX)
public class OllamaEmbeddingProperties {

    public static final String CONFIG_PREFIX = "langchain4j.ollama.embedding";

    /**
     * The name of the model to use.
     */
    private String model = "llama2";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
