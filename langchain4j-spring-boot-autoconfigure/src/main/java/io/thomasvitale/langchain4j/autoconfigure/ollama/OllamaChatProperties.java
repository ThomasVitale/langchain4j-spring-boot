package io.thomasvitale.langchain4j.autoconfigure.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.ollama.api.Options;

/**
 * Configuration properties for Ollama chat models.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OllamaChatProperties.CONFIG_PREFIX)
public class OllamaChatProperties {

    public static final String CONFIG_PREFIX = "langchain4j.ollama.chat";

    /**
     * Name of the model to use.
     */
    private String model = "llama2";

    /**
     * Format to return a response in. Supported: 'json'.
     */
    private String format;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
