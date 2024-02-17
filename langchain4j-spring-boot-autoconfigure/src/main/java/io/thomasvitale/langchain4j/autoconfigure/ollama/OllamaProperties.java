package io.thomasvitale.langchain4j.autoconfigure.ollama;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.thomasvitale.langchain4j.spring.core.model.client.ClientConfig;

/**
 * Common configuration properties for Ollama.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(OllamaProperties.CONFIG_PREFIX)
public class OllamaProperties {

    public static final String CONFIG_PREFIX = "langchain4j.ollama";

    /**
     * Base URL where the Ollama API server is running.
     */
    private URI baseUrl = URI.create("http://localhost:11434");

    /**
     * Settings for the HTTP client.
     */
    @NestedConfigurationProperty
    private ClientConfig client = new ClientConfig();

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ClientConfig getClient() {
        return client;
    }

    public void setClient(ClientConfig client) {
        this.client = client;
    }

}
