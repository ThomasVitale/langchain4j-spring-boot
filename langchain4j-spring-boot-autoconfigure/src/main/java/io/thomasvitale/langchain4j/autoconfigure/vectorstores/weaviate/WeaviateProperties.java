package io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore;

/**
 * Configuration properties for Weaviate.
 */
@ConfigurationProperties(WeaviateProperties.CONFIG_PREFIX)
public class WeaviateProperties {

    public static final String CONFIG_PREFIX = "langchain4j.vectorstore.weaviate";

    /**
     * Whether to enable the Weaviate integration.
     */
    private boolean enabled = true;

    /**
     * Weaviate object class to use.
     */
    private String objectClassName = WeaviateEmbeddingStore.DEFAULT_OBJECT_CLASS_NAME;

    /**
     * Strategy for eventual consistency.
     */
    private String consistencyLevel = WeaviateEmbeddingStore.DEFAULT_CONSISTENCY_LEVEL;

    /**
     * Chroma Client configuration.
     */
    private Client client = new Client();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getObjectClassName() {
        return objectClassName;
    }

    public void setObjectClassName(String objectClassName) {
        this.objectClassName = objectClassName;
    }

    public String getConsistencyLevel() {
        return consistencyLevel;
    }

    public void setConsistencyLevel(String consistencyLevel) {
        this.consistencyLevel = consistencyLevel;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class Client {

        /**
         * URL where the Weaviate server is running.
         */
        private URI url = URI.create("http://localhost:8080");

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
         * API key for token authentication.
         */
        private String apiKey;

        /**
         * Additional headers to include in each request to Weaviate.
         */
        Map<String, String> headers = Map.of();

        public URI getUrl() {
            return url;
        }

        public void setUrl(URI url) {
            this.url = url;
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

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
    }

}
