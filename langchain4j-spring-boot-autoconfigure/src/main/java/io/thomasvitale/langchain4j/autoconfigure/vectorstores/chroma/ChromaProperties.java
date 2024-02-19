package io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.thomasvitale.langchain4j.spring.chroma.ChromaEmbeddingStore;

/**
 * Configuration properties for Chroma.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(ChromaProperties.CONFIG_PREFIX)
public class ChromaProperties {

    public static final String CONFIG_PREFIX = "langchain4j.vectorstore.chroma";

    /**
     * Chroma collection to use.
     */
    private String collectionName = ChromaEmbeddingStore.DEFAULT_COLLECTION_NAME;

    /**
     * Chroma Client configuration.
     */
    private Client client = new Client();

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class Client {

        /**
         * Base URL where the Chroma server is running.
         */
        private URI url = URI.create("http://localhost:8000");

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
         * API token for token authentication.
         */
        private String apiToken;

        /**
         * Username for basic authentication.
         */
        private String username;

        /**
         * Password for basic authentication.
         */
        private String password;

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

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}
