package io.thomasvitale.langchain4j.autoconfigure.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.ollama.api.Options;

/**
 * Configuration properties for Ollama embedding models.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OllamaEmbeddingProperties.CONFIG_PREFIX)
public class OllamaEmbeddingProperties {

    public static final String CONFIG_PREFIX = "langchain4j.ollama.embedding";

    /**
     * Name of the model to use.
     */
    private String model = "llama2";

    /**
     * Additional model parameters.
     */
    @NestedConfigurationProperty
    private Options options = Options.create();

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
