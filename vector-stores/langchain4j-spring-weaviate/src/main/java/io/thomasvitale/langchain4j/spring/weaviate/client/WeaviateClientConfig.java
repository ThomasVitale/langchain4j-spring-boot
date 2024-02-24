package io.thomasvitale.langchain4j.spring.weaviate.client;

import java.time.Duration;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Options for configuring the Weaviate client.
 *
 * @author Thomas Vitale
 */
public record WeaviateClientConfig(
        String scheme,
        String host,
        Duration connectTimeout,
        Duration readTimeout,
        @Nullable
        String sslBundle,
        @Nullable
        String apiKey,
        @Nullable
        Map<String, String> headers
){

    public WeaviateClientConfig {
        Assert.hasText(scheme, "scheme must not be null or empty");
        Assert.hasText(host, "host must not be null or empty");
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String scheme = "http";
        private String host = "localhost:8080";
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration readTimeout = Duration.ofSeconds(60);
        @Nullable
        private String sslBundle;
        @Nullable
        private String apiKey;
        @Nullable
        private Map<String, String> headers;

        private Builder() {}

        public Builder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
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

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public WeaviateClientConfig build() {
            return new WeaviateClientConfig(scheme, host, connectTimeout, readTimeout, sslBundle, apiKey, headers);
        }
    }

}
