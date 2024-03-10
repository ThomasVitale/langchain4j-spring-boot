package io.thomasvitale.langchain4j.spring.weaviate.client;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Options for configuring the Weaviate client.
 */
public record WeaviateClientConfig(
        URI url,
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
        Assert.notNull(url, "url must not be null");
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private URI url = URI.create("http://localhost:8080");
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration readTimeout = Duration.ofSeconds(60);
        @Nullable
        private String sslBundle;
        @Nullable
        private String apiKey;
        @Nullable
        private Map<String, String> headers;

        private Builder() {}

        public Builder url(URI url) {
            this.url = url;
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
            return new WeaviateClientConfig(url, connectTimeout, readTimeout, sslBundle, apiKey, headers);
        }
    }

}
