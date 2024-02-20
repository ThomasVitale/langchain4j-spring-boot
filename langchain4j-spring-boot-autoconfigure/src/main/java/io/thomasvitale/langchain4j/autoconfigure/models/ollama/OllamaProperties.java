package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

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
     * Whether to enable the Ollama integration.
     */
    private boolean enabled = true;

    /**
     * Settings for the HTTP client.
     */
    private Client client = new Client();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class Client {

        /**
         * Base URL where the Ollama API server is running.
         */
        private URI baseUrl = URI.create("http://localhost:11434");

        /**
         * Maximum time to wait for a connection.
         */
        private Duration connectTimeout = Duration.ofSeconds(10);

        /**
         * Maximum time to wait for a response.
         */
        private Duration readTimeout = Duration.ofSeconds(60);

        /**
         * SSL certificate bundle to use to establish a secure connection.
         */
        private String sslBundle;

        /**
         * Whether to log requests.
         */
        private boolean logRequests = false;

        /**
         * Whether to log responses.
         */
        private boolean logResponses = false;

        public URI getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(URI baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Duration getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }

        public String getSslBundle() {
            return sslBundle;
        }

        public void setSslBundle(String sslBundle) {
            this.sslBundle = sslBundle;
        }

        public boolean isLogRequests() {
            return logRequests;
        }

        public void setLogRequests(boolean logRequests) {
            this.logRequests = logRequests;
        }

        public boolean isLogResponses() {
            return logResponses;
        }

        public void setLogResponses(boolean logResponses) {
            this.logResponses = logResponses;
        }

    }

}
