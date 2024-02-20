package io.thomasvitale.langchain4j.spring.ollama.client;

import java.net.URI;
import java.time.Duration;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Options for configuring the Ollama client.
 *
 * @author Thomas Vitale
 */
public record OllamaClientConfig(
//@formatter:off
        URI baseUrl,
        Duration connectTimeout,
        Duration readTimeout,
        @Nullable
        String sslBundle,
        boolean logRequests,
        boolean logResponses
//@formatter:on
) {

    public OllamaClientConfig {
        Assert.notNull(baseUrl, "baseUrl must not be null");
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private URI baseUrl = URI.create("http://localhost:11434");

        private Duration connectTimeout = Duration.ofSeconds(10);

        private Duration readTimeout = Duration.ofSeconds(60);

        @Nullable
        private String sslBundle;

        private boolean logRequests = false;

        private boolean logResponses = false;

        private Builder() {
        }

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

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaClientConfig build() {
            return new OllamaClientConfig(baseUrl, connectTimeout, readTimeout, sslBundle, logRequests, logResponses);
        }

    }

}
