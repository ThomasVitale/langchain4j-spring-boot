package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import java.net.URI;
import java.time.Duration;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Common configuration properties for the OpenAI clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(OpenAiProperties.CONFIG_PREFIX)
public class OpenAiProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai";

    /**
     * Whether to enable the OpenAI integration.
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
         * Base URL of the OpenAI API.
         */
        private URI baseUrl = URI.create("https://api.openai.com");

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
         * OpenAI APY Key.
         */
        private String apiKey;

        /**
         * Which organization to use for OpenAI requests.
         */
        private String organizationId;

        /**
         * Unique identifier representing your end-user, which can help OpenAI to monitor and
         * detect abuse.
         */
        private String user;

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

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
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
