package io.thomasvitale.langchain4j.autoconfigure.ollama;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * Timeout for Ollama calls.
     */
    private Duration timeout = Duration.ofSeconds(30);

    /**
     * The maximum number of times an Ollama call will be retried.
     */
    private Integer maxRetries = 3;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

}
