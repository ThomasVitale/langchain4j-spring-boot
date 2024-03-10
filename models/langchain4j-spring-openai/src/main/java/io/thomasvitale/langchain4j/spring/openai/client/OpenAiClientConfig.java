package io.thomasvitale.langchain4j.spring.openai.client;

import java.net.URI;
import java.time.Duration;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Options for configuring the OpenAI client.
 */
public record OpenAiClientConfig(
        URI baseUrl,
        Duration connectTimeout,
        Duration readTimeout,
        @Nullable
        String sslBundle,
        String apiKey,
        String organizationId,
        String user,
        boolean logRequests,
        boolean logResponses
) {

    public OpenAiClientConfig {
        Assert.notNull(baseUrl, "baseUrl must not be null");
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
        Assert.hasText(apiKey, "apiKey must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private URI baseUrl = URI.create("https://api.openai.com");
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration readTimeout = Duration.ofSeconds(60);
        private String apiKey;
        private String organizationId;
        private String user;
        @Nullable
        private String sslBundle;
        private boolean logRequests = false;
        private boolean logResponses = false;

        private Builder() {}

        public Builder baseUrl(URI baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder sslBundle(String sslBundle) {
            this.sslBundle = sslBundle;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiClientConfig build() {
            return new OpenAiClientConfig(baseUrl, connectTimeout, readTimeout, sslBundle, apiKey, organizationId, user, logRequests, logResponses);
        }
    }

}
